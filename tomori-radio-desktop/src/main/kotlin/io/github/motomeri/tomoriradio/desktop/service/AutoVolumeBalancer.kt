package io.github.motomeri.tomoriradio.desktop.service

import javafx.animation.AnimationTimer
import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.runBlocking
import net.transgressoft.commons.music.player.AudioItemPlayer
import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class AutoVolumeBalancer(
    private val loudnessService: LoudnessAnalysisService,
    private val trackPlayService: TrackPlayService,
    private val fxPlaybackService: FXPlaybackService
) {

    private val targetLoudness = 0.5
    private val smoothFactor = 0.1
    private val targetVolume = SimpleDoubleProperty(0.8)

    private val timer = object : AnimationTimer() {
        private var lastUpdateNano = 0L
        private val updateInterval = 100_000_000L // 100ms

        override fun handle(now: Long) {
            if (now - lastUpdateNano < updateInterval) return
            lastUpdateNano = now

            // 仅在播放状态下进行调整
            if (fxPlaybackService.player.status() != AudioItemPlayer.Status.PLAYING) {
                return
            }

            // 获取当前播放的 AudioItem 和播放位置
            val currentItem = trackPlayService.currentPlaying ?: return
            val currentTime = fxPlaybackService.player.currentTimeProperty.get().toMillis()
            val totalDuration = fxPlaybackService.player.totalDuration.toMillis()

            if (totalDuration <= 0) return

            // 1. 获取当前时间点附近的振幅数据
            val amplitudes = runBlocking { //TODO: runBlocking?
                loudnessService.getAmplitudes(currentItem)
            }
            
            // 2. 计算当前位置的响度, 取 -250~+250ms 内的平均值
            val windowSizeMs = 500.0 
            val startIndex = ((currentTime - windowSizeMs/2) / totalDuration * amplitudes.size).toInt().coerceAtLeast(0)
            val endIndex = ((currentTime + windowSizeMs/2) / totalDuration * amplitudes.size).toInt().coerceAtMost(amplitudes.size - 1)
            
            var sum = 0.0
            for (i in startIndex..endIndex) {
                sum += abs(amplitudes[i].toDouble())
            }
            val avgAmplitude = sum / (endIndex - startIndex + 1)

            // 3. 根据响度计算目标音量
            // 响度低 -> 提高音量; 响度高 -> 降低音量
            val newTargetVolume = (targetLoudness / (avgAmplitude + 0.01)).coerceIn(0.0, 1.0)
            
            // 4. 平滑过渡, 避免音量突变
            targetVolume.set(targetVolume.get() + (newTargetVolume - targetVolume.get()) * smoothFactor)
            
            // 5. 应用音量到播放器
            fxPlaybackService.player.setVolume(targetVolume.get())
        }
    }

    fun start() {
        timer.start()
    }

    fun stop() {
        timer.stop()
        // 恢复默认音量
        fxPlaybackService.player.setVolume(0.8)
    }
}