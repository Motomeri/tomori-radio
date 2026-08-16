plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa")
    kotlin("plugin.spring")
    kotlin("plugin.serialization") version "2.0.0"
}

group = "io.github.motomeri"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("com.h2database:h2")
    implementation("io.github.vinceglb:auto-launch:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("io.github.xxfast:kstore:1.1.0")
    implementation("io.github.xxfast:kstore-file:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("io.mockk:mockk:1.14.11")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}