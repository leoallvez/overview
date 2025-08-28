import com.android.build.api.dsl.VariantDimension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.8.0"
}

android {
    namespace = "${libs.versions.app.id.get()}.data"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        stringField(name = "API_URL", value = "https://api.themoviedb.org/3/")
        stringField(name = "API_KEY", value = System.getenv("OVER_API_KEY"))
        buildConfigField(type = "int", name = "PAGE_SIZE", value = "20")
    }
    buildFeatures {
        buildConfig = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.hilt.android)
    api(libs.retrofit)
    
    // Room
    api(libs.room.runtime)
    api(libs.room.ktx)
    api(libs.room.paging)

    // DataStore
    api(libs.datastore.preferences)

    api(libs.kotlinx.serialization.json)
    api(libs.network.response.adapter)

    implementation(project(":core"))
    implementation(project(":domain"))

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

fun VariantDimension.stringField(name: String, value: String) {
    buildConfigField(type = "String", name = name, value = "\"$value\"")
}