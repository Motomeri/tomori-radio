package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.domain.MusicSlotEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 播放档期的数据库.
 *
 * @author RikkaKawaii0612
 */
interface MusicSlotRepository : JpaRepository<MusicSlotEntity, Long>