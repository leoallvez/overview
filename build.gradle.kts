// Top-level build file where you can add configuration options common to all sub-projects/modules.
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

plugins {
    id("com.google.devtools.ksp") version "1.7.20-1.0.8" apply false
}

tasks.register<Delete>(name = "clean") {
    delete(layout.buildDirectory)
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
