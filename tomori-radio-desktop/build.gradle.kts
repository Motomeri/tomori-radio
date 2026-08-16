plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openjfx.javafxplugin") version "0.1.0"
    kotlin("plugin.serialization") version "2.0.0"
    kotlin("plugin.spring")
}

group = "io.github.motomeri"
version = "0.0.1-SNAPSHOT"

val musicCommonsVersion = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":tomori-radio-core"))
    implementation("org.openjfx:javafx-controls:25.0.3")
    implementation("org.openjfx:javafx-fxml:25.0.3")
    implementation("org.openjfx:javafx-media:25.0.3")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    implementation("net.transgressoft:music-commons-api:${musicCommonsVersion}")
    implementation("net.transgressoft:music-commons-core:${musicCommonsVersion}")
    implementation("net.transgressoft:music-commons-fx:${musicCommonsVersion}")
    implementation("net.transgressoft:music-commons-media:${musicCommonsVersion}")
    implementation("net.transgressoft:music-commons-persistence:${musicCommonsVersion}")
    implementation("net.transgressoft:music-commons-persistence-fx:${musicCommonsVersion}")
    implementation("com.dustinredmond.fxtrayicon:FXTrayIcon:4.2.3")
    testImplementation(kotlin("test"))
}

javafx {
    version = "25.0.3"
    modules = listOf("javafx.controls", "javafx.fxml")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}