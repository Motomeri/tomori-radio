package io.github.motomeri.tomoriradio.core.track.service

import io.github.motomeri.tomoriradio.core.track.domain.ScheduledTrackEntity
import org.springframework.data.jpa.repository.JpaRepository
import kotlin.time.Instant

/**
 * 已排期曲目的数据库.
 *
 * @author RikkaKawaii0612
 */
interface ScheduledTrackRepository: JpaRepository<ScheduledTrackEntity, Long> {

    fun findFirstByOrderByScheduledTimeAsc(): ScheduledTrackEntity?

}