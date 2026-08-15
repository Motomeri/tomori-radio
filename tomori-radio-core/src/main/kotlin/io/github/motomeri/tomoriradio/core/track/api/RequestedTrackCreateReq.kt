package io.github.motomeri.tomoriradio.core.track.api

data class RequestedTrackCreateReq(
    var title: String,
    var artist: String,
    var length: Long,
    var requester: String
)