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
}

android {
    val appId = "br.dev.singular.overview"
    namespace = appId
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appId
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // API keys and URLs
        stringField(name = "API_KEY", value = System.getenv("OVER_API_KEY"))
        stringField(name = "API_URL", value = "https://api.themoviedb.org/3/")
        stringField(name = "IMG_URL", value = "https://image.tmdb.org/t/p/w780")
        stringField(name = "THUMBNAIL_BASE_URL", value = "https://img.youtube.com/vi")
        stringField(name = "THUMBNAIL_QUALITY", value = "hqdefault.jpg")
        // Build configurations
        buildConfigField(type = "boolean", name = "ADS_ARE_VISIBLE", value = "true")
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
    // Signing configurations
    val activeSigning = System.getenv("OVER_ACTIVE_SIGNING") == "true"
    if (activeSigning) {
        // Signing configurations for different environments
        signingConfigs {
            create("prd") {
                storeFile = rootProject.file(System.getenv("OVER_PRD_KEYSTORE"))
                storePassword = System.getenv("OVER_PRD_PASSWORD")
                keyAlias = System.getenv("OVER_PRD_KEY_ALIAS")
                keyPassword = System.getenv("OVER_PRD_PASSWORD")
            }
            create("hmg") {
                storeFile = rootProject.file(System.getenv("OVER_HMG_KEYSTORE"))
                storePassword = System.getenv("OVER_HMG_PASSWORD")
                keyAlias = System.getenv("OVER_HMG_KEY_ALIAS")
                keyPassword = System.getenv("OVER_HMG_PASSWORD")
            }
        }
    }
    // Build types configurations
    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
            if (activeSigning) {
                signingConfig = signingConfigs.getByName(name = "hmg")
            }
        }
        create("prd") {
            setAppName(appName = "app_name_prd")
            if (activeSigning) {
                signingConfig = signingConfigs.getByName(name = "prd")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
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
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Paging
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    // Google Ads
    implementation(libs.play.services.ads)

    // Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.systemuicontroller)

    // Third-party libraries
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.network.response.adapter)
    implementation(libs.toolbar.compose)
    implementation(libs.progress.indicator)
    implementation(libs.youtube.player)

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

// extensions
fun VariantDimension.stringField(name: String, value: String?) {
    buildConfigField(type = "String", name = name, value = "\"${value ?: ""}\"")
}

fun ApplicationProductFlavor.setAppName(appName: String) {
    resValue(type = "string", name = "app_name", value = "@string/$appName")
}
