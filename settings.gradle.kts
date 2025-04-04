@file:Suppress("UnstableApiUsage")

include(":app")
include(":data")
include(":domain")
include(":firebase")
include(":presentation")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Overview"
