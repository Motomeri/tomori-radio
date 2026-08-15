package io.github.motomeri.tomoriradio.core.settings

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class AppSettings(
    val interval: Duration = 5.seconds
)