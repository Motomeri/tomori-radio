package io.github.motomeri.tomoriradio

import io.github.motomeri.tomoriradio.desktop.JavaFxApp
import javafx.application.Application
import javafx.stage.Stage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["io.github.motomeri.tomoriradio"])
class Application

fun main(args: Array<String>) {
    val context = runApplication<Application>(*args)
    Application.launch(JavaFxApp::class.java, *args)
}
