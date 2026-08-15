package io.github.motomeri.tomoriradio.core.track.domain

import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

/**
 * 点歌曲目实体.
 *
 * @param id        曲目 ID
 * @param title     曲目标题
 * @param artist    曲目作者
 * @param length    曲目时长, 单位: ns
 * @param requester 点歌人
 */
data class RequestedTrack(
    var id: Long,
    var title: String,
    var artist: String,
    var length: Long,
    var requester: String
) {

    /**
     * [Duration] 形式的曲目时长.
     */
    val duration: Duration get() = length.nanoseconds

    /**
     * 获取曲目的显示名称, 格式为 `title - artist`.
     */
    fun getName(): String {
        return "$title - $artist"
    }

    /**
     * 针对数据库实体的扩展方法.
     */
    companion object {

        fun RequestedTrackEntity.toDto(): RequestedTrack = RequestedTrack(id ?: 0L, title, artist, length, requester)

    }

}