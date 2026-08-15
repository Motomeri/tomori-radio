package io.github.motomeri.tomoriradio.core.track.service

import io.github.motomeri.tomoriradio.core.track.domain.RequestedTrackEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 已点曲目的数据库.
 *
 * @author RikkaKawaii0612
 */
interface RequestedTrackRepository : JpaRepository<RequestedTrackEntity, Long> {

    fun findByArtist(artist: String): RequestedTrackEntity?

    fun findByTitle(title: String): RequestedTrackEntity?

    fun findByArtistAndTitle(artist: String, title: String): RequestedTrackEntity?

    fun existsByTitleAndArtist(title: String, artist: String): Boolean

}