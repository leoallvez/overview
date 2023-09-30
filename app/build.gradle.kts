plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
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

    implementation(platform("androidx.compose:compose-bom:2022.12.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.runtime:runtime-livedata")

    implementation("androidx.lifecycle:lifecycle-runtime")
    implementation("androidx.activity:activity-compose:1.8.0-rc01")
    // Firebase Module
    implementation(project(path = ":firebase"))
    // Lifecycle
    val lifecycle = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.3")
    // Hilt
    val hilt = "2.46.1"
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    // Room
    val room = "2.5.2"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    ksp("androidx.room:room-compiler:$room")
    // Paging
    val pagingVersion = "3.2.1"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingVersion")
    // WorkManager
    val workVersion = "2.8.1"
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // Google Ads
    implementation("com.google.android.gms:play-services-ads:22.4.0")
    // Accompanist
    val accompanist = "0.30.1"
    implementation("com.google.accompanist:accompanist-pager:$accompanist")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanist")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanist")

    // Third party library
    implementation("com.jakewharton.timber:timber:5.0.1")
    val retrofit = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // noinspection GradleDependency
    implementation("com.github.haroldadmin:NetworkResponseAdapter:4.1.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("me.onebone:toolbar-compose:2.3.5")
    implementation("com.ehsanmsz:msz-progress-indicator:0.2.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("org.amshove.kluent:kluent-android:1.73")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.0-alpha06")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
