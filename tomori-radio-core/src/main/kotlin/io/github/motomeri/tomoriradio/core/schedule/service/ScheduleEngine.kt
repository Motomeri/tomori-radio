package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.domain.MusicSlot
import io.github.motomeri.tomoriradio.core.schedule.domain.TrackSchedule
import io.github.motomeri.tomoriradio.core.track.domain.RequestedTrack
import io.github.motomeri.tomoriradio.core.track.domain.ScheduledTrack
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Instant

/**
 * 处理曲目排期算法的组件服务.
 *
 * 排期算法由 `DeepSeek-V4-Flash` 倾情开发, 为我们优化出平均接近 `O(T * log(T))` 的时间复杂度.
 *
 * @author RikkaKawaii0612
 */
@Component
class ScheduleEngine {

    /**
     * 为输入的点歌曲目进行全量排期.
     *
     * @param slots    播放档期列表, 需从星期一 0:00 开始, 按档期起始时间排序
     * @param tracks   要排期的曲目列表, 需剔除超出单个档期时长的曲目
     * @param now      排期的起始点
     * @param interval 两个相邻曲目之间的最小间隔
     *
     * @return 已排期曲目快照的列表
     */
    fun scheduleAllTracks(
        slots: List<MusicSlot>,
        tracks: List<RequestedTrack>,
        now: Instant,
        interval: Duration
    ) = schedule(
        existingScheduled = null,
        trackMap = null,
        slots = slots,
        newTracks = tracks,
        now = now,
        interval = interval
    )

    /**
     * 为新增至列表末尾的点歌曲目进行增量排期.
     *
     * @param existingScheduled 已有排期曲目列表
     * @param trackMap          点歌曲目 ID 到点歌曲目的映射, 用于获取时长
     * @param slots             播放档期列表, 需从星期一 0:00 开始, 按档期起始时间排序
     * @param tracks            要排期的曲目列表, 需剔除超出单个档期时长的曲目
     * @param now               排期的起始点
     * @param interval          两个相邻曲目之间的最小间隔
     *
     * @return 新增已排期曲目快照的列表
     */
    fun scheduleAdditionalTracks(
        existingScheduled: List<ScheduledTrack>?,
        trackMap: Map<Long, RequestedTrack>?,
        slots: List<MusicSlot>,
        tracks: List<RequestedTrack>,
        now: Instant,
        interval: Duration
    ) = schedule(
        existingScheduled = existingScheduled,
        trackMap = trackMap,
        slots = slots,
        newTracks = tracks,
        now = now,
        interval = interval
    )

    // ---------- 增量排期方法 ----------
    private fun schedule(
        existingScheduled: List<ScheduledTrack>?,
        trackMap: Map<Long, RequestedTrack>?,
        slots: List<MusicSlot>,
        newTracks: List<RequestedTrack>,
        now: Instant,
        interval: Duration
    ): List<TrackSchedule> {

        // ---------- 辅助数据 ----------
        data class OccupiedInterval(val start: Instant, val end: Instant)

        data class FreeSegment(val start: Instant, val end: Instant) {
            val duration: Duration get() = end - start
        }

        // 扩展函数: 获取某周周一 0:00 UTC
        fun DayOfWeek.daysOffsetFromMonday(): Int =
            when (this) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> 1
                DayOfWeek.WEDNESDAY -> 2
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 4
                DayOfWeek.SATURDAY -> 5
                DayOfWeek.SUNDAY -> 6
            }

        fun LocalTime.toDuration(): Duration = toNanoOfDay().nanoseconds

        fun getWeekStart(instant: Instant): Instant {
            val javaInstant = java.time.Instant.ofEpochSecond(
                instant.epochSeconds,
                instant.nanosecondsOfSecond.toLong()
            )
            val localDate = java.time.LocalDateTime.ofInstant(javaInstant, ZoneOffset.UTC).toLocalDate()
            val daysSinceMonday = (localDate.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
            val mondayDate = localDate.minusDays(daysSinceMonday.toLong())
            val mondayInstant = mondayDate.atStartOfDay().toInstant(ZoneOffset.UTC)
            return Instant.fromEpochSeconds(mondayInstant.epochSecond, mondayInstant.nano.toLong())
        }

        fun mergeIntervals(intervals: List<OccupiedInterval>): List<OccupiedInterval> {
            if (intervals.isEmpty()) return emptyList()
            val sorted = intervals.sortedBy { it.start }
            val merged = mutableListOf<OccupiedInterval>()
            var current = sorted[0]
            for (i in 1 until sorted.size) {
                val next = sorted[i]
                if (next.start <= current.end) {
                    current = current.copy(end = maxOf(current.end, next.end))
                } else {
                    merged.add(current)
                    current = next
                }
            }
            merged.add(current)
            return merged
        }

        fun getFreeSegments(
            start: Instant,
            end: Instant,
            occupied: List<OccupiedInterval>
        ): List<FreeSegment> {
            val result = mutableListOf<FreeSegment>()
            var current = start
            for ((start1, end1) in occupied) {
                if (end1 <= current) continue
                if (start1 >= end) break
                if (start1 > current) {
                    val segEnd = minOf(start1, end)
                    if (segEnd > current) {
                        result.add(FreeSegment(current, segEnd))
                    }
                }
                current = maxOf(current, end1)
                if (current >= end) break
            }
            if (current < end) {
                result.add(FreeSegment(current, end))
            }
            return result
        }

        // ---------- 1. 构建初始占用区间 ----------
        val occupied = mutableListOf<OccupiedInterval>()
        if (existingScheduled != null) {
            requireNotNull(trackMap) { "trackMap must be provided when existingScheduled is given" }

            for ((_, trackId, scheduledTime) in existingScheduled) {
                val track = trackMap[trackId] ?: error("Track $trackId not found in trackMap")
                val dur = track.duration
                val occStart = scheduledTime - interval
                val occEnd = scheduledTime + dur + interval
                occupied.add(OccupiedInterval(occStart, occEnd))
            }
        }
        val merged = mergeIntervals(occupied)
        occupied.clear()
        occupied.addAll(merged)

        // ---------- 2. 初始化空闲段池 ----------
        val minDuration = newTracks.minOfOrNull { it.duration } ?: Duration.ZERO    // 最短曲目, 用于剔除碎片
        val freeMap = TreeMap<Instant, FreeSegment>()           // 键为起始时间, 保持全局有序

        // 向池中添加空闲段, 自动合并相邻段, 并丢弃无用碎片
        fun addFreeSegment(start: Instant, end: Instant) {
            if (end <= start) return
            if (end - start < minDuration) return              // 优化: 容纳不下任何曲目, 直接丢弃

            var s = start
            var e = end

            // 1. 检查是否能与后续段合并 (边界相接)
            val next = freeMap.ceilingEntry(s)
            if (next != null && next.key == e) {
                freeMap.remove(next.key)
                e = next.value.end
            }

            // 2. 检查是否能与前序段合并 (边界相接)
            val prev = freeMap.floorEntry(s)
            if (prev != null && prev.value.end == s) {
                freeMap.remove(prev.key)
                s = prev.key
            }

            freeMap[s] = FreeSegment(s, e)
        }

        // 生成某一周的所有空闲段, 并加入池中
        var currentWeekStart = getWeekStart(now)

        fun generateNextWeek() {
            for (slot in slots) {
                val dayOffset = slot.dayOfWeek.daysOffsetFromMonday().days
                val slotStart = currentWeekStart + dayOffset + slot.from.toDuration()
                val slotEnd = slotStart + slot.duration
                val effectiveStart = if (slotStart < now) now else slotStart
                if (slotEnd <= effectiveStart) continue
                // 从原始时段中减去已占用的区间
                val freeSegments = getFreeSegments(effectiveStart, slotEnd, occupied)
                for ((start, end) in freeSegments) {
                    addFreeSegment(start, end)
                }
            }
            currentWeekStart += 7.days
        }

        generateNextWeek()   // 生成第一周

        // ---------- 3. 排期新曲目 ----------
        val scheduled = mutableListOf<TrackSchedule>()

        for (track in newTracks) {
            var found = false
            while (!found) {
                var entry = freeMap.firstEntry()
                while (entry != null) {
                    val seg = entry.value
                    if (seg.duration >= track.duration) {
                        found = true
                        freeMap.remove(entry.key)
                        val candidateTime = seg.start
                        scheduled.add(TrackSchedule(track.id, candidateTime))

                        // 计算保护区间的结束点
                        val protectedEnd = candidateTime + track.duration + interval
                        // 将剩余空闲段放回池中
                        addFreeSegment(protectedEnd, seg.end)

                        // 更新全局占用区间
                        val newOcc = OccupiedInterval(candidateTime - interval, protectedEnd)
                        occupied.add(newOcc)
                        occupied.clear()
                        occupied.addAll(mergeIntervals(occupied))
                        break
                    }
                    entry = freeMap.higherEntry(entry.key)
                }
                if (!found) {
                    generateNextWeek()   // 当前池中无可用段, 生成下一周
                }
            }
        }

        // ---------- 4. 按时间排序返回 ----------
        return scheduled.sortedBy { it.scheduledTime }
    }
}