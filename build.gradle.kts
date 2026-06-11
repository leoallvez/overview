// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.hilt.android.plugin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kover)
}

dependencies {
    kover(project(":app"))
    kover(project(":core"))
    kover(project(":data"))
    kover(project(":domain"))
    kover(project(":presentation"))
}

// Global coverage configuration
kover {
    reports {
        filters {
            excludes {
                annotatedBy(
                    "*Preview",
                    "*UiComponentPreview",
                    "*UiScreenPreview",
                )
                classes(
                    "*.BuildConfig",
                    "*.*_Factory*",
                    "*.*_HiltModules*",
                    "*.*_Impl*",
                    "*.CustomApplication",
                    "*.Hilt_*",
                    "**.ui.theme.**",
                    "**.*PreviewKt*",
                    "**.*Activity*",
                    "**.*Fragment*",
                    "**.*_HiltModules*",
                    "**.*_Provide*Factory*",
                )
            }
        }
        total {
            xml { onCheck = true }
            html { title = "Overview Project Coverage" }
        }
    }
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.compose.compiler.gradle.plugin)
    }
}
