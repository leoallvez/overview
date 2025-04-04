// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Repositories configuration
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri(path = "https://jitpack.io")
        }
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.ktlint.gradle)
        classpath(libs.firebase.crashlytics.gradle)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

// Apply plugins
plugins {
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false // Apply KSP plugin with apply false
}

// Custom clean task
tasks.register<Delete>(name = "clean") {
    delete(layout.buildDirectory)
}
// Kotlin plugin
allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri(path = "https://jitpack.io")
        }
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
