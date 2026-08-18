package io.github.motomeri.tomoriradio.core.schedule.domain

import kotlin.time.Instant

/**
 * 经排期服务排期过的点歌曲目.
 *
 * @param id            在数据库中的 ID
 * @param trackId       对应已点曲目的 ID
 * @param scheduledTime 曲目排期
 */
data class ScheduledTrack(
    val id: Long,
    val trackId: Long,
    var scheduledTime: Instant
) {

    /**
     * 针对数据库实体的扩展方法.
     */
    companion object {

        fun ScheduledTrackEntity.toDto(): ScheduledTrack = ScheduledTrack(id ?: 0L, trackId, scheduledTime)

    }

}