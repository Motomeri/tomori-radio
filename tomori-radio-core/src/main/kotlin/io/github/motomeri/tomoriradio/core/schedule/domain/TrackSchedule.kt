package io.github.motomeri.tomoriradio.core.schedule.domain

import kotlin.time.Instant

/**
 * 已排期曲目的快照, 不含数据库 ID, 用于排期算法的返回值.
 *
 * @see io.github.motomeri.tomoriradio.core.track.domain.ScheduledTrack
 * @see io.github.motomeri.tomoriradio.core.schedule.service.ScheduleEngine
 *
 * @author RikkaKawaii0612
 */
data class TrackSchedule(
    val trackId: Long,
    val scheduledTime: Instant
)