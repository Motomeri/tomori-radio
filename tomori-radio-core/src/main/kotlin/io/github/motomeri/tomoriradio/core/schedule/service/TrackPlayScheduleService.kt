package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.Scheduler
import io.github.motomeri.tomoriradio.core.track.service.ScheduledTrackService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Service
import kotlin.time.Instant

/**
 * 已排期曲目的定时播放通知服务.
 *
 * 该服务从 [io.github.motomeri.tomoriradio.core.track.service.ScheduledTrackService] 动态获取已排期曲目, 并在启动定时计划后按排期播放.
 *
 * @see io.github.motomeri.tomoriradio.core.schedule.service.TrackPlaybackService
 * @see io.github.motomeri.tomoriradio.core.track.service.ScheduledTrackService
 *
 * @author RikkaKawaii0612
 */
@Service
class TrackPlayScheduleService(
    val scheduledTrackService: ScheduledTrackService,
    val trackPlaybackService: TrackPlaybackService
) {

    private val logger = KotlinLogging.logger {}

    private val scheduler = Scheduler(
        scope = CoroutineScope(Dispatchers.IO),
        onTick = { onTick(it) }
    )

    fun onTick(instant: Instant) {
        val track = scheduledTrackService.getFirstScheduledTrack()
        if (track != null) {
            trackPlaybackService.notifyPlayback(track.trackId)
            logger.info { "Notifying track playback event (trackId = ${track.trackId}) at $instant" }
        } else {
            logger.warn { "Trying to notify track playback event at $instant, but no scheduled track present" }
        }
    }
}