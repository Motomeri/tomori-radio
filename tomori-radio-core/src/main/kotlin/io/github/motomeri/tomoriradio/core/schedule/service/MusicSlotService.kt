package io.github.motomeri.tomoriradio.core.schedule.service

import io.github.motomeri.tomoriradio.core.schedule.api.MusicSlotCreateReq
import io.github.motomeri.tomoriradio.core.schedule.domain.MusicSlot
import io.github.motomeri.tomoriradio.core.schedule.domain.MusicSlot.Companion.toDto
import io.github.motomeri.tomoriradio.core.schedule.domain.MusicSlotEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 管理音乐播放档期的服务.
 *
 * @author RikkaKawaii0612
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class MusicSlotService(
    private val musicSlotRepository: MusicSlotRepository
) {

    @Transactional(readOnly = true)
    fun getAllSlots(): List<MusicSlot> = musicSlotRepository.findAll().map { it.toDto() }.sorted()

    /**
     * 向指定星期添加一个播放档期, 如果档期没有重叠 (允许边界重叠).
     *
     * 由于添加档期被设计为只有唯一前端入口, 没有多线程并发问题, 因此不需要加锁.
     *
     * @param createReq 档期添加请求
     * @return `true` 若添加的档期不与现有档期重叠; 否则返回 `false`
     */
    @Transactional
    fun addSlot(createReq: MusicSlotCreateReq): Boolean {
        val entity = MusicSlotEntity(
            dayOfWeek = createReq.dayOfWeek,
            from = createReq.from,
            to = createReq.to
        )
        val dto = entity.toDto()

        musicSlotRepository.findAll().forEach {
            if (it.toDto().intersects(dto)) return false
        }

        musicSlotRepository.save(entity)

        return true
    }

    /**
     * 从指定星期移除指定播放档期.
     *
     * @param slot 档期
     * @return `true` 若存在对应的播放档期
     */
    @Transactional
    fun removeSlot(slot: MusicSlot): Boolean {
        val existed = musicSlotRepository.existsById(slot.id)
        musicSlotRepository.deleteById(slot.id)
        return existed
    }

}