package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.domain.ScheduledTrackEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 已排期曲目的数据库.
 *
 * @author RikkaKawaii0612
 */
interface ScheduledTrackRepository: JpaRepository<ScheduledTrackEntity, Long> {

    fun findFirstByOrderByScheduledTimeAsc(): ScheduledTrackEntity?

}