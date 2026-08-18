package io.github.motomeri.tomoriradio.core.track.domain

import jakarta.persistence.*
import java.net.URL
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

@Entity
@Table(name = "requested_tracks", indexes = [
    Index(name = "idx_requested_track_artist", columnList = "artist"),
    Index(name = "idx_requested_track_title", columnList = "title")
])
class RequestedTrackEntity(
    @Id @GeneratedValue(GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false, length = 200) var title: String,
    @Column(nullable = false, length = 100) var artist: String,
    @Column(nullable = false) var length: Long,
    @Column(nullable = false, length = 100, name = "file_url") var fileUrl: URL
) {

    /**
     * [Duration] 形式的曲目时长.
     */
    @get:Transient
    val duration: Duration get() = length.nanoseconds

    /**
     * 获取曲目的显示名称, 格式为 `title - artist`.
     */
    fun getName(): String {
        return "$title - $artist"
    }

}