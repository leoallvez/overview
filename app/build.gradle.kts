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
    namespace = "br.dev.singular.overview"
    compileSdk = 34

    defaultConfig {

        applicationId = "br.dev.singular.overview"
        minSdk = 21
        targetSdk = 33
        versionCode = 200
        versionName = "2.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }

        val apiKey = System.getenv("API_KEY")
        buildConfigField(type = "String", name = "API_KEY", value = "\"$apiKey\"")
        buildConfigField(type = "String", name = "API_URL", value = "\"https://api.themoviedb.org/3/\"")
        buildConfigField(type = "String", name = "IMG_URL", value = "\"https://image.tmdb.org/t/p/w780\"")
        buildConfigField(type = "boolean", name = "ADS_ARE_VISIBLES", value = "true")
    }
    val activeSigning = System.getenv("ACTIVE_SIGNING") == "true"
    if (activeSigning) {
        signingConfigs {
            create("prod") {
                storeFile = rootProject.file(System.getenv("OVER_PROD_STORE_FILE"))
                storePassword = System.getenv("OVER_PROD_PASSWORD")
                keyAlias = System.getenv("OVER_PROD_KEY_ALIAS")
                keyPassword = System.getenv("OVER_PROD_PASSWORD")
            }
            create("homol") {
                storeFile = rootProject.file(System.getenv("OVER_HOMOL_STORE_FILE"))
                storePassword = System.getenv("OVER_HOMOL_PASSWORD")
                keyAlias = System.getenv("OVER_HOMOL_KEY_ALIAS")
                keyPassword = System.getenv("OVER_HOMOL_PASSWORD")
            }
        }
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(type = "String", name = "DEBUG_BANNER_ID", value = "\"\"")
        }
        named("debug") {
            buildConfigField(type = "String", name = "DEBUG_BANNER_ID", value = "\"ca-app-pub-3940256099942544/6300978111\"")
        }
    }
    flavorDimensions.add("version")
    productFlavors {
        create("dev") {
            resValue(type = "string", name = "app_name", value = "@string/app_name_dev")
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("homol") {
            resValue(type = "string", name = "app_name", value = "@string/app_name_homol")
            dimension = "version"
            applicationIdSuffix = ".homol"
            versionNameSuffix = "-homol"
            if (activeSigning) {
                signingConfig = signingConfigs.getByName(name = "homol")
            }
        }
        create("prod") {
            resValue(type = "string", name = "app_name", value = "@string/app_name_prod")
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
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.runtime.livedata)

    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)
    // Firebase Module
    implementation(project(path = ":firebase"))
    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata)
    // Navigation
    implementation(libs.navigation.compose)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
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

    // Third party library
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)
    // noinspection GradleDependency
    implementation(libs.network.response.adapter)
    implementation(libs.coil.compose)
    implementation(libs.toolbar.compose)
    implementation(libs.progress.indicator)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.compose.tooling)
}
