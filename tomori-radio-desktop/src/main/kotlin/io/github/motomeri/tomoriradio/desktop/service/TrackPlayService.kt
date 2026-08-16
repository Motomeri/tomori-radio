package io.github.motomeri.tomoriradio.desktop.service

import io.github.motomeri.tomoriradio.core.schedule.event.TrackPlaybackEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import net.transgressoft.commons.music.audio.AudioItem
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class TrackPlayService(
    private val fxPlaybackService: FXPlaybackService,
    private val trackManagementService: TrackManagementService
) {

    private val logger = KotlinLogging.logger {}

    final var currentPlaying: AudioItem? = null
        private set

    @EventListener
    fun onTrackPlaybackEvent(event: TrackPlaybackEvent) {
        val audioItem = trackManagementService.findById(event.trackId)
        if (audioItem == null) {
            logger.warn { "No audio item with id ${event.trackId} present" }
            return
        }

        currentPlaying = audioItem
        fxPlaybackService.player.play(audioItem)
    }

}