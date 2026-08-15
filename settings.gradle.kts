pluginManagement {
    plugins {
        kotlin("plugin.spring") version "2.3.21"
        kotlin("plugin.jpa") version "2.3.21"
    }
}
rootProject.name = "tomori-radio"
include("tomori-radio-core")
include("tomori-radio-desktop")