plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = 34
    namespace = "io.github.leoallvez.firebase"
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("long", "REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS", "3600")
        }
        debug {
            buildConfigField("long", "REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS", "0")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Firebase
    api(platform("com.google.firebase:firebase-bom:31.0.1"))
    api("com.google.firebase:firebase-config-ktx")
    api("com.google.firebase:firebase-crashlytics-ktx")
    api("com.google.firebase:firebase-analytics-ktx")

    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.5.0")
}