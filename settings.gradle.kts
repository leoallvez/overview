pluginManagement {
    plugins {
        kotlin("jvm") version "2.2.10"
        kotlin("plugin.serialization") version "2.2.20"
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "overview"
include(":app")
include(":data")
include(":presentation")
include(":domain")
include(":core")
