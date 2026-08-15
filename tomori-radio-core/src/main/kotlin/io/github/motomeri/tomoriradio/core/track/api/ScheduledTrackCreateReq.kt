package io.github.motomeri.tomoriradio.core.track.api

import kotlin.time.Instant

data class ScheduledTrackCreateReq(
    val trackId: Long,
    var scheduledTime: Instant
)
