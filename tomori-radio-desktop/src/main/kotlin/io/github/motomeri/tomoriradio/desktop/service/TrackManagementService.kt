package io.github.motomeri.tomoriradio.desktop.service

import io.github.motomeri.tomoriradio.desktop.model.DetailedTrack
import kotlinx.coroutines.runBlocking
import net.transgressoft.commons.music.CoreMusicLibrary
import net.transgressoft.commons.music.audio.AudioItem
import net.transgressoft.commons.persistence.music.audio.AudioItemMapSerializer
import net.transgressoft.lirp.persistence.json.JsonFileRepository
import org.springframework.stereotype.Service
import java.io.File
import kotlin.time.Clock
import kotlin.time.Instant

@Service
class TrackManagementService {

    private val coreMusicLibrary = CoreMusicLibrary.builder()
        .audioRepository(JsonFileRepository(
            File("/audio-library.json").apply { parentFile.mkdirs(); createNewFile() },
            AudioItemMapSerializer))
        .build()

    private val trackIdsToAudioIds = mutableMapOf<Long, String>()

    private val detailedTracks = mutableMapOf<Long, DetailedTrack>()


    init {
        runBlocking {
            importTrack(
                1L,
                "a",
                "b",
                Clock.System.now(),
                File("D:/_sys/music2/songs/00. まっしろな雪 (feat. 水瀬ましろ) - Halozy.mp3")
            )
        }
    }

    suspend fun importTrack(id: Long, title: String, artist: String, schedule: Instant, file: File) {
        val item = coreMusicLibrary.audioLibrary().createFromFile(file.toPath())
        trackIdsToAudioIds[id] = item.uniqueId
        detailedTracks[id] = DetailedTrack(
            id = id,
            title = title,
            artist = artist,
            cover = item.coverImageBytes,
            schedule = schedule
        )
    }

    fun getTracks() = detailedTracks.values.toList()

    fun findById(id: Long): AudioItem? {
        return trackIdsToAudioIds[id]?.let { coreMusicLibrary.audioLibrary().findByUniqueId(it) }
            ?.orElse(null)
    }

}