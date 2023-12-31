import com.android.build.api.dsl.LibraryBuildType

fun LibraryBuildType.setRemoteInterval(value: String) {
    buildConfigField(type = "long", name = "REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS", value = value)
}

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = libs.versions.compile.sdk.get().toInt()
    namespace = "io.github.leoallvez.firebase"
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            setRemoteInterval("3600")
        }
        debug {
            setRemoteInterval("0")
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
    implementation(libs.androidx.core)
    implementation(libs.appcompat)

    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.remote.config.ktx)
    api(libs.crashlytics.ktx)
    api(libs.analytics.ktx)

    implementation(libs.timber)

    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
}
