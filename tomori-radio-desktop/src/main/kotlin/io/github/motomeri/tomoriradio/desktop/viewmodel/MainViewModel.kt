package io.github.motomeri.tomoriradio.desktop.viewmodel

import io.github.motomeri.tomoriradio.desktop.util.getValue
import io.github.motomeri.tomoriradio.desktop.util.setValue
import javafx.beans.property.SimpleStringProperty
import org.springframework.stereotype.Component

@Component
class MainViewModel {

    val currentScreenProperty = SimpleStringProperty("home")

    var currentScreen: String by currentScreenProperty

}