package io.github.motomeri.tomoriradio.core.schedule.api

import java.time.DayOfWeek
import java.time.LocalTime

data class MusicSlotCreateReq(
    val dayOfWeek: DayOfWeek,
    val from: LocalTime,
    val to: LocalTime
)