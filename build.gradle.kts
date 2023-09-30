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
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:11.5.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
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