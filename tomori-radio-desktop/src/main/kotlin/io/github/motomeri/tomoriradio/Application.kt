package io.github.motomeri.tomoriradio

import io.github.motomeri.tomoriradio.desktop.JavaFxApp
import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["io.github.motomeri.tomoriradio"])
class Application

fun main(args: Array<String>) {
    Application.launch(JavaFxApp::class.java, *args)
}
