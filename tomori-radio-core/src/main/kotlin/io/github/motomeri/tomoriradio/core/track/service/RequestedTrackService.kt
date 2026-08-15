package io.github.motomeri.tomoriradio.core.track.service

import io.github.motomeri.tomoriradio.core.track.api.RequestedTrackCreateReq
import io.github.motomeri.tomoriradio.core.track.domain.RequestedTrack
import io.github.motomeri.tomoriradio.core.track.domain.RequestedTrack.Companion.toDto
import io.github.motomeri.tomoriradio.core.track.domain.RequestedTrackEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 管理点歌曲目的服务.
 *
 * @author RikkaKawaii0612
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class RequestedTrackService(
    private val trackRepository: RequestedTrackRepository
) {

    /**
     * 添加曲目.
     *
     * @return `true` 若没有重复的曲目, 并成功添加
     */
    @Transactional
    fun addTrack(createReq: RequestedTrackCreateReq): Boolean {
        if (trackRepository.existsByTitleAndArtist(createReq.title, createReq.artist)) return false
        trackRepository.save(RequestedTrackEntity(
            title = createReq.title,
            artist = createReq.artist,
            length = createReq.length,
            requester = createReq.requester
        ))
        return true
    }

    /**
     * 移除已有曲目.
     *
     * @return `true` 若指定曲目原先存在, 并成功移除
     */
    @Transactional
    fun removeTrack(id: Long): Boolean {
        val existed = trackRepository.existsById(id)
        trackRepository.deleteById(id)
        return existed
    }

    /**
     * 移除已有曲目.
     *
     * @return `true` 若指定曲目原先存在, 并成功移除
     */
    @Transactional
    fun removeTrack(requestedTrack: RequestedTrack) = removeTrack(requestedTrack.id)

    /**
     * 移除一批已有曲目.
     */
    @Transactional
    fun removeTracks(ids: Collection<Long>) = trackRepository.deleteAllById(ids)

    /**
     * 移除一批已有曲目.
     */
    @Transactional
    fun removeTracks(requestedTracks: Collection<RequestedTrack>) {
        removeTracks(requestedTracks.map { it.id })
    }
    /**
     * 获取所有曲目.
     */
    @Transactional(readOnly = true)
    fun getAllTracks() = trackRepository.findAll().map { it.toDto() }
    /**
     * 根据 ID 获取曲目.
     */
    @Transactional(readOnly = true)
    fun getTrack(id: Long) = trackRepository.findByIdOrNull(id)?.toDto()

}