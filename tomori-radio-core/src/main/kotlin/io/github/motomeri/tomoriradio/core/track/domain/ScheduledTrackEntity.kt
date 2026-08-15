package io.github.motomeri.tomoriradio.core.track.domain

import jakarta.persistence.*
import kotlin.time.Instant

@Entity
@Table(name = "scheduled_tracks")
class ScheduledTrackEntity(
    @Id @GeneratedValue(GenerationType.IDENTITY)
    var id: Long? = null,

    @JoinColumn(name = "track_id", unique = true, nullable = false)
    var trackId: Long,

    @Column(nullable = false, name = "scheduled_time")
    var scheduledTime: Instant
)