package io.github.motomeri.tomoriradio.core.schedule.domain

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime

@Entity
class MusicSlotEntity(
    @Id @GeneratedValue(GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false, name = "day_of_week") var dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    @Column(nullable = false) var from: LocalTime = LocalTime.MIN,
    @Column(nullable = false) var to: LocalTime = LocalTime.MIN
)
