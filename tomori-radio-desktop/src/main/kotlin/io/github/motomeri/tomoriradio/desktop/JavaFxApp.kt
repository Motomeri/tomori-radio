package io.github.motomeri.tomoriradio.desktop

import com.dustinredmond.fxtrayicon.FXTrayIcon
import io.github.motomeri.tomoriradio.desktop.controller.MainController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication

class JavaFxApp : Application() {

    override fun start(primaryStage: Stage) {
        val context = runApplication<Application>()

        initTray(primaryStage)
        val controller = context.getBean<MainController>()

        val loader = FXMLLoader(javaClass.getResource("/fxml/main.fxml"))
        loader.setController(controller)

        val root = loader.load<Parent>()
        primaryStage.title = "Tomori Radio"
        primaryStage.scene = Scene(root, 600.0, 400.0)
        primaryStage.show()
        primaryStage.show()
    }

    fun initTray(primaryStage: Stage) {
        val trayIcon = FXTrayIcon.Builder(primaryStage, javaClass.getResource("/tray_icon.png"))
            .build()

        trayIcon.setOnAction {
            primaryStage.isShowing.let {
                if (it) primaryStage.hide() else primaryStage.show()
            }
        }

        trayIcon.show()
    }

}
