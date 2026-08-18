package io.github.motomeri.tomoriradio.core.schedule.api

import kotlin.time.Instant

data class ScheduledTrackCreateReq(
    val trackId: Long,
    var scheduledTime: Instant
)
