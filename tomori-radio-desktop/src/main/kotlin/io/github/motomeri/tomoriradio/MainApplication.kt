package io.github.motomeri.tomoriradio

import io.github.motomeri.tomoriradio.desktop.JavaFxApp
import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["io.github.motomeri.tomoriradio"])
class MainApplication

fun main(args: Array<String>) {
    System.setProperty("java.awt.headless", "false")
    Application.launch(JavaFxApp::class.java, *args)
}
