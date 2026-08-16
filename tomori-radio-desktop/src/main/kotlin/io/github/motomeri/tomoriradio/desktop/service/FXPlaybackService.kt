package io.github.motomeri.tomoriradio.desktop.service

import net.transgressoft.commons.fx.music.player.FXAudioItemPlayer
import org.springframework.stereotype.Service

@Service
class FXPlaybackService {

    val player = FXAudioItemPlayer()

}