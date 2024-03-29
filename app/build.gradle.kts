import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.VariantDimension

// extensions
fun VariantDimension.stringField(name: String, value: String?) {
    buildConfigField(type = "String", name = name, value = "\"${value ?: ""}\"")
}

fun ApplicationProductFlavor.setAppName(appName: String) {
    resValue(type = "string", name = "app_name", value = "@string/$appName")
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

android {
    val id = "br.dev.singular.overview"
    namespace = id
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        applicationId = id
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
        // API keys and URLs
        stringField(name = "TMDB_API_KEY", value = System.getenv("OVER_TMDB_API_KEY"))
        stringField(name = "TMDB_API_URL", value = "https://api.themoviedb.org/3/")
        stringField(name = "TMDB_IMG_URL", value = "https://image.tmdb.org/t/p/w780")

        // Build configurations
        buildConfigField(type = "boolean", name = "ADS_ARE_VISIBLE", value = "true")
        buildConfigField(type = "int", name = "PAGE_SIZE", value = "20")
    }
    // Signing configurations
    val activeSigning = System.getenv("OVER_ACTIVE_SIGNING") == "true"
    if (activeSigning) {
        // Signing configurations for different environments
        signingConfigs {
            create("prod") {
                storeFile = rootProject.file(System.getenv("OVER_PROD_KEYSTORE"))
                storePassword = System.getenv("OVER_PROD_PASSWORD")
                keyAlias = System.getenv("OVER_PROD_KEY_ALIAS")
                keyPassword = System.getenv("OVER_PROD_PASSWORD")
            }
            create("homol") {
                storeFile = rootProject.file(System.getenv("OVER_HOMOL_KEYSTORE"))
                storePassword = System.getenv("OVER_HOMOL_PASSWORD")
                keyAlias = System.getenv("OVER_HOMOL_KEY_ALIAS")
                keyPassword = System.getenv("OVER_HOMOL_PASSWORD")
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
        create("homol") {
            setAppName(appName = "app_name_homol")
            dimension = "version"
            applicationIdSuffix = ".homol"
            versionNameSuffix = "-homol"
            if (activeSigning) {
                signingConfig = signingConfigs.getByName(name = "homol")
            }
        }
        create("prod") {
            setAppName(appName = "app_name_prod")
            if (activeSigning) {
                signingConfig = signingConfigs.getByName(name = "prod")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    hilt {
        enableAggregatingTask = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.runtime.livedata)

    // Lifecycle
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata)

    // Firebase Module
    implementation(project(path = ":firebase"))

    // Navigation
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
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
    kapt(libs.hilt.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    // Google Ads
    implementation(libs.play.services.ads)

    // Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.flowlayout)

    // Third-party libraries
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.network.response.adapter)
    implementation(libs.coil.compose)
    implementation(libs.toolbar.compose)
    implementation(libs.progress.indicator)

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Test dependencies
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)

    // Debug-specific dependencies
    debugImplementation(libs.compose.tooling)
}
