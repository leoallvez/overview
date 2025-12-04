import com.android.build.api.dsl.VariantDimension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "${libs.versions.app.id.get()}.presentation"
    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField(type = "int", name = "PAGE_SIZE", value = "20")
        stringField(name = "IMG_URL", value = "https://image.tmdb.org/t/p/w780")
        stringField(name = "THUMBNAIL_BASE_URL", value = "https://img.youtube.com/vi")
        stringField(name = "THUMBNAIL_QUALITY", value = "hqdefault.jpg")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            stringField(name = "DEBUG_BANNER_ID", value = "")
        }
        debug {
            stringField(name = "DEBUG_BANNER_ID", value = "ca-app-pub-3940256099942544/6300978111")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(project(":domain"))

    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.coil.compose)
    api(libs.hilt.navigation.compose)
    implementation(libs.material)
    api(libs.kotlinx.collections.immutable)
    implementation(libs.progress.indicator)

    // Google Ads
    api(libs.play.services.ads)

    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)

    // Paging
    api(libs.paging.runtime.ktx)
    api(libs.paging.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
}

private fun VariantDimension.stringField(name: String, value: String) {
    buildConfigField(type = "String", name = name, value = "\"$value\"")
}
