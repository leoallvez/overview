// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Repositories configuration
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri(path = "https://plugins.gradle.org/m2/")
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
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
}

// Custom clean task
tasks.register<Delete>(name = "clean") {
    delete(layout.buildDirectory)
}

// Apply Ktlint to all projects
allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
