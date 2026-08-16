package io.github.motomeri.tomoriradio.desktop.viewmodel

import io.github.motomeri.tomoriradio.core.schedule.event.ScheduleUpdatedEvent
import io.github.motomeri.tomoriradio.desktop.model.DetailedTrack
import io.github.motomeri.tomoriradio.desktop.service.TrackManagementService
import javafx.collections.FXCollections
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class TracksViewModel(
    private val trackManagementService: TrackManagementService
) {

    val tracks = FXCollections.observableArrayList<DetailedTrack>()!!

    @EventListener
    private fun onScheduleUpdated(event: ScheduleUpdatedEvent) {
        tracks.setAll(trackManagementService.getTracks())
    }

}