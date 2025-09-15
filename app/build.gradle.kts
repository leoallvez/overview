import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.VariantDimension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("plugin.serialization") version "2.2.20"
}

android {
    namespace = libs.versions.app.id.get()
    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        applicationId = libs.versions.app.id.get()
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(type = "int", name = "PAGE_SIZE", value = "20")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    if (isActiveSigning()) {
        // Signing configurations for different environments
        signingConfigs {
            val home = System.getProperty("user.home")
            create("prd") {
                storeFile = rootProject.file("$home${System.getenv("OVER_PRD_KEYSTORE")}")
                storePassword = System.getenv("OVER_PRD_PASSWORD")
                keyAlias = System.getenv("OVER_PRD_KEY_ALIAS")
                keyPassword = System.getenv("OVER_PRD_PASSWORD")
            }
            create("hmg") {
                storeFile = rootProject.file("$home${System.getenv("OVER_HMG_KEYSTORE")}")
                storePassword = System.getenv("OVER_HMG_PASSWORD")
                keyAlias = System.getenv("OVER_HMG_KEY_ALIAS")
                keyPassword = System.getenv("OVER_HMG_PASSWORD")
            }
        }
    }
    // Build types configurations
    buildTypes {
        named("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            stringField(name = "DEBUG_BANNER_ID", value = "")
        }
        named("debug") {
            stringField(name = "DEBUG_BANNER_ID", value = "ca-app-pub-3940256099942544/6300978111")
        }
    }
    flavorDimensions.add("version")
    productFlavors {
        create("dev") {
            setAppName(appName = "app_name_dev")
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("hmg") {
            setAppName(appName = "app_name_hmg")
            dimension = "version"
            applicationIdSuffix = ".homol"
            versionNameSuffix = "-hmg"
            if (isActiveSigning()) {
                signingConfig = signingConfigs.getByName(name = "hmg")
            }
        }
        create("prd") {
            setAppName(appName = "app_name_prd")
            if (isActiveSigning()) {
                signingConfig = signingConfigs.getByName(name = "prd")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    hilt {
        enableAggregatingTask = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)

    // Lifecycle
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room
    ksp(libs.room.compiler)

    // Paging
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    // Google Ads
    implementation(libs.play.services.ads)

    // Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.systemuicontroller)

    // Third-party libraries
    implementation(libs.timber)
    implementation(libs.logging.interceptor)
    implementation(libs.toolbar.compose)
    implementation(libs.progress.indicator)
    implementation(libs.youtube.player)
    implementation(libs.converter.moshi)
    implementation(libs.converter.serialization)

    // Modules
    implementation(project(path = ":core"))
    implementation(project(path = ":data"))
    implementation(project(path = ":domain"))
    implementation(project(path = ":presentation"))

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

private fun VariantDimension.stringField(name: String, value: String) {
    buildConfigField(type = "String", name = name, value = "\"$value\"")
}

private fun ApplicationProductFlavor.setAppName(appName: String) {
    resValue(type = "string", name = "app_name", value = "@string/$appName")
}

private fun isActiveSigning() = System.getenv("OVER_ACTIVE_SIGNING") == "true"
