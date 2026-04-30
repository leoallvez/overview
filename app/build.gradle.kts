import com.android.build.api.dsl.ApplicationProductFlavor
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
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
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("int", "PAGE_SIZE", "20")
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

    flavorDimensions.add("version")
    productFlavors {
        create("dev") {
            setAppName("app_name_dev")
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("hmg") {
            setAppName("app_name_hmg")
            dimension = "version"
            applicationIdSuffix = ".homol"
            versionNameSuffix = "-hmg"
            if (isActiveSigning()) {
                signingConfig = signingConfigs.getByName("hmg")
            }
        }
        create("prd") {
            setAppName("app_name_prd")
            dimension = "version"
            if (isActiveSigning()) {
                signingConfig = signingConfigs.getByName("prd")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = true
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
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

    // Room
    ksp(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    // Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.flowlayout)

    // Third-party libraries
    implementation(libs.timber)
    implementation(libs.logging.interceptor)
    implementation(libs.toolbar.compose)
    implementation(libs.converter.moshi)
    implementation(libs.converter.serialization)

    // Modules
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

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

private fun ApplicationProductFlavor.setAppName(appName: String) {
    resValue("string", "app_name", "@string/$appName")
}

private fun isActiveSigning() = System.getenv("OVER_ACTIVE_SIGNING") == "true"
