package io.github.motomeri.tomoriradio.core.schedule.domain

import io.github.motomeri.tomoriradio.core.util.toLocalDateTimeInUTC
import java.time.DayOfWeek
import java.time.LocalTime
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Instant

/**
 * 表示音乐播放档期的数据库实体. 时段为半开半闭区间 `[from, to)`, 且允许跨天, 最长可持续 `24h`.
 * 当 `from >= to` 时, 时段跨天, 即 `to` 表示下一天的时间点.
 */
data class MusicSlot(
    val id: Long,
    val dayOfWeek: DayOfWeek,
    val from: LocalTime,
    val to: LocalTime
) : Comparable<MusicSlot> {

    /**
     * 获取该档期的时长.
     */
    val duration: Duration
        get() {
            val delta = (to.toNanoOfDay() - from.toNanoOfDay()).nanoseconds

            return if (isOvernight()) {
                delta + 24.hours
            } else {
                delta
            }
        }

    /**
     * 判断档期是否跨天.
     */
    fun isOvernight(): Boolean = from >= to

    /**
     * 判断档期是否包含指定时间点.
     */
    fun contains(instant: Instant): Boolean {
        val localDateTime = instant.toLocalDateTimeInUTC()
        val localTime = localDateTime.toLocalTime()
        return if (!isOvernight()) {
            dayOfWeek == localDateTime.dayOfWeek && localTime in from..<to
        } else {
            when (dayOfWeek) {
                localDateTime.dayOfWeek -> {
                    localTime >= from
                }
                localDateTime.dayOfWeek + 1 -> {
                    localTime < to
                }
                else -> false
            }
        }
    }

    /**
     * 比较档期开始的时间先后.
     */
    override operator fun compareTo(other: MusicSlot): Int {
        return compareValuesBy(this, other, { it.dayOfWeek }, { it.from })
    }

    /**
     * 判断两个档期是否有交集 (不含边界).
     */
    fun intersects(other: MusicSlot): Boolean {
        val dayNanos = 24L * 60 * 60 * 1_000_000_000
        val weekNanos = 7 * dayNanos

        fun startNanos(slot: MusicSlot): Long =
            slot.dayOfWeek.value.toLong() * dayNanos + slot.from.toNanoOfDay()

        val start1 = startNanos(this)
        val end1 = start1 + duration.inWholeNanoseconds

        val start2 = startNanos(other)
        val end2 = start2 + other.duration.inWholeNanoseconds

        for (offset in listOf(-weekNanos, 0L, weekNanos)) {
            val s2 = start2 + offset
            val e2 = end2 + offset
            if (max(start1, s2) < min(end1, e2)) {
                return true
            }
        }

        return false
    }

    /**
     * 针对数据库实体的扩展方法.
     */
    companion object {

        fun MusicSlotEntity.toDto(): MusicSlot = MusicSlot(id ?: 0L, dayOfWeek, from, to)

    }

}