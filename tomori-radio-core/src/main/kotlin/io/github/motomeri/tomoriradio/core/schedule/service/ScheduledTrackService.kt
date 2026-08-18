package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.event.ScheduleUpdatedEvent
import io.github.motomeri.tomoriradio.core.settings.service.SettingService
import io.github.motomeri.tomoriradio.core.schedule.domain.ScheduledTrack.Companion.toDto
import io.github.motomeri.tomoriradio.core.schedule.domain.ScheduledTrackEntity
import io.github.motomeri.tomoriradio.core.track.service.RequestedTrackService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.time.Instant

/**
 * 管理已排期曲目的服务.
 *
 * @author RikkaKawaii0612
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class ScheduledTrackService(
    private val requestedTrackService: RequestedTrackService,
    private val musicSlotService: MusicSlotService,
    private val settingService: SettingService,
    private val scheduledTrackRepository: ScheduledTrackRepository,
    private val scheduleEngine: ScheduleEngine,
    private val eventPublisher: ApplicationEventPublisher
) {

    private val logger = KotlinLogging.logger {}

    /**
     * 从传入的时间开始, 为当前所有已点曲目进行排期, 并存入数据库.
     *
     * @param startTime 开始时间
     */
    @Transactional
    fun scheduleAll(startTime: Instant) {
        // TODO: 把这部分逻辑处理职责也分离到 ScheduleEngine
        val tracks = requestedTrackService.getAllTracks()
        tracks.ifEmpty {
            logger.info { "Schedule finished. No tracks found in database" }
            return
        }

        val slots = musicSlotService.getAllSlots()
        val maxDuration = slots.maxOfOrNull { it.duration } ?: run {
            logger.info { "Schedule finished. No available slots found in database" }
            return
        }

        val (removed, filteredTracks) = tracks.partition { it.duration >= maxDuration }
        removed.forEach { logger.info { "Song '${it.getName()}' cannot be scheduled because no slot fits. Skipped" } }

        val interval = settingService.getSettings().interval
        val scheduled = scheduleEngine.scheduleAllTracks(slots, filteredTracks, startTime, interval)
        logger.info { "Schedule finished. ${scheduled.size} scheduled" }

        scheduledTrackRepository.deleteAll()
        scheduledTrackRepository.saveAll(scheduled.map {
            ScheduledTrackEntity(trackId = it.trackId, scheduledTime = it.scheduledTime)
        })

        eventPublisher.publishEvent(ScheduleUpdatedEvent())
    }

    /**
     * 获取所有已排期曲目.
     */
    @Transactional(readOnly = true)
    fun getScheduledTracks() = scheduledTrackRepository.findAll().map { it.toDto() }

    /**
     * 获取当前最早的已排期曲目.
     */
    @Transactional(readOnly = true)
    fun getFirstScheduledTrack() = scheduledTrackRepository.findFirstByOrderByScheduledTimeAsc()

}