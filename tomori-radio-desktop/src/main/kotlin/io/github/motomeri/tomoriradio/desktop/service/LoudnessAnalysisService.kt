package io.github.motomeri.tomoriradio.desktop.service

import net.transgressoft.commons.media.waveform.ScalableAudioWaveform
import net.transgressoft.commons.music.audio.AudioItem
import org.springframework.stereotype.Service
import kotlin.io.path.Path

@Service
class LoudnessAnalysisService {

    private val amplitudeCache = mutableMapOf<Int, FloatArray>()

    suspend fun getAmplitudes(audioItem: AudioItem, width: Int = 1000): FloatArray {
        return amplitudeCache.getOrPut(audioItem.id) {
            val path = Path(audioItem.fileName)
            val waveform = ScalableAudioWaveform(audioItem.id, path)
            waveform.amplitudes(width = width, height = 100)
        }
    }
}