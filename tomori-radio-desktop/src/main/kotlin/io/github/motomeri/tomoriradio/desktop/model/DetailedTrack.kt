package io.github.motomeri.tomoriradio.desktop.model

import io.github.motomeri.tomoriradio.desktop.util.getValue
import io.github.motomeri.tomoriradio.desktop.util.setValue
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import kotlin.time.Instant

class DetailedTrack(
    id: Long,
    title: String,
    artist: String,
    cover: ByteArray?,
    schedule: Instant
) {

    val idProperty = SimpleLongProperty(id)
    val titleProperty = SimpleStringProperty(title)
    val artistProperty = SimpleStringProperty(artist)
    val coverProperty = SimpleObjectProperty(cover)
    val scheduleProperty = SimpleObjectProperty(schedule)

    var id: Long by idProperty
    var title: String by titleProperty
    var artist: String by artistProperty
    var cover: ByteArray? by coverProperty
    var schedule: Instant by scheduleProperty

}