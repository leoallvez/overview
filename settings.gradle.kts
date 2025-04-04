@file:Suppress("UnstableApiUsage")

include(":presentation")
include(":domain")
include(":app")
include(":firebase")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Overview"
