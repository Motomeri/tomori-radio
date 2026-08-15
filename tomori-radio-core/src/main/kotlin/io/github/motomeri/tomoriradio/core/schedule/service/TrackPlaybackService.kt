package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.event.TrackPlaybackEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

/**
 * 播放已排期曲目的服务.
 *
 * 外部可使用 Spring 的事件监听注解, 对 [TrackPlaybackEvent]
 * 进行监听.
 *
 * @see io.github.motomeri.tomoriradio.core.track.domain.ScheduledTrack
 *
 * @author RikkaKawaii0612
 */
@Service
class TrackPlaybackService(
    private val publisher: ApplicationEventPublisher
) {

    /**
     * 通知播放某一已排期曲目.
     */
    fun notifyPlayback(trackId: Long) {
        publisher.publishEvent(TrackPlaybackEvent(trackId))
    }

}