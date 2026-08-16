package io.github.motomeri.tomoriradio.desktop

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.net.URL

@Component
class SpringFXMLLoader(
    private val context: ApplicationContext
) {

    fun load(location: URL): Parent {
        val loader = FXMLLoader(location)
        loader.setControllerFactory {
            controllerClass -> context.getBean(controllerClass)
        }
        return loader.load()
    }

}