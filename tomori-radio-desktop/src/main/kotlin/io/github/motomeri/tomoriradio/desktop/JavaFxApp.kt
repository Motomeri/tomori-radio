package io.github.motomeri.tomoriradio.desktop

import com.dustinredmond.fxtrayicon.FXTrayIcon
import io.github.motomeri.tomoriradio.MainApplication
import io.github.motomeri.tomoriradio.desktop.controller.MainController
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication

class JavaFxApp : Application() {

    override fun start(primaryStage: Stage) {
        val context = SpringApplication.run(MainApplication::class.java)

        initTray(primaryStage)

        val root = context.getBean<SpringFXMLLoader>().load(javaClass.getResource("/fxml/main.fxml")!!)
        primaryStage.title = "Tomori Radio"
        primaryStage.scene = Scene(root, 600.0, 400.0)
        primaryStage.show()
        Platform.setImplicitExit(false)
    }

    fun initTray(primaryStage: Stage) {
        val trayIcon = FXTrayIcon.Builder(primaryStage, javaClass.getResource("/images/tomori.jpg"))
            .build()

        trayIcon.setOnAction {
            primaryStage.isShowing.let {
                if (!it) {
                    primaryStage.show()
                    primaryStage.toFront()
                }
            }
        }

        trayIcon.show()
    }

}
