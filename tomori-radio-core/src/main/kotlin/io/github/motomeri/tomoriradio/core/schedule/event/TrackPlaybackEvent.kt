package io.github.motomeri.tomoriradio.core.schedule.event

import org.springframework.context.ApplicationEvent

/**
 * 点歌曲目播放事件.
 *
 * @param trackId 要播放的已点曲目 ID
 */
data class TrackPlaybackEvent(
    val trackId: Long
) : ApplicationEvent(trackId)
