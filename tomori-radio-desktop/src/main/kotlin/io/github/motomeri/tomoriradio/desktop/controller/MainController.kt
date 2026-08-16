package io.github.motomeri.tomoriradio.desktop.controller

import io.github.motomeri.tomoriradio.desktop.SpringFXMLLoader
import io.github.motomeri.tomoriradio.desktop.viewmodel.MainViewModel
import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import org.springframework.stereotype.Component

@Component
class MainController(
    private val viewModel: MainViewModel,
    private val fxmlLoader: SpringFXMLLoader
) {

    private val logger = KotlinLogging.logger {}

    @FXML lateinit var background: StackPane
    @FXML lateinit var contentArea: StackPane
    @FXML lateinit var buttonGroup: HBox
    @FXML lateinit var trackCountLabel: Label
    @FXML lateinit var topToolbar: HBox

    private val viewCache = mutableMapOf<String, Parent>()

    @FXML
    fun initialize() {
        viewModel.currentScreenProperty.addListener { _, _, newScreen ->
            newScreen?.let { switchScreenTo(it) }
        }

        switchScreenTo(viewModel.currentScreen)
    }

    private fun switchScreenTo(screen: String) {
        val root = viewCache.getOrPut(screen) {
            loadFxmlForScreen(screen)
        }

        contentArea.children.setAll(root)
    }

    private fun loadFxmlForScreen(screen: String): Parent {
        val resourcePath = "/fxml/$screen.fxml"
        val resource = javaClass.getResource(resourcePath)
            ?: return Label("Unknown screen: $screen")

        return try {
            fxmlLoader.load(resource)
        } catch (e: Exception) {
            logger.error(e) { "Failed to load screen" }
            Label("Error loading screen: ${e.stackTraceToString()}")
        }
    }

    @FXML
    fun onHome() {
        viewModel.currentScreen = "home"
    }

    @FXML
    fun onTracks() {
        viewModel.currentScreen = "tracks"
    }

    @FXML
    fun onRequest() {
        viewModel.currentScreen = "request"
    }

    @FXML
    fun onSettings() {
        viewModel.currentScreen = "settings"
    }

}