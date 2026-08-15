package io.github.motomeri.tomoriradio.core.util

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.time.Instant
import kotlin.time.toJavaInstant


/**
 * 获取 [Instant] 在 UTC0 下的日期时间.
 */
fun Instant.toLocalDateTimeInUTC(): LocalDateTime {
    val javaInstant = this.toJavaInstant()
    val utcDateTime = javaInstant.atOffset(ZoneOffset.UTC)
    return utcDateTime.toLocalDateTime()
}
