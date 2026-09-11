import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.VariantDimension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kover)
    alias(libs.plugins.paparazzi)
}

extensions.configure<LibraryExtension> {
    val sdkCompile = libs.versions.sdk.compile.get().toInt()
    val sdkMin = libs.versions.sdk.min.get().toInt()

    namespace = "${libs.versions.app.id.get()}.presentation"
    compileSdk = sdkCompile

    defaultConfig {
        minSdk = sdkMin
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("int", "PAGE_SIZE", "20")
        stringField("IMG_URL", "https://image.tmdb.org/t/p/w780")
        stringField("POSTER_URL", "https://image.tmdb.org/t/p/w154")
        stringField("THUMBNAIL_BASE_URL", "https://img.youtube.com/vi")
        stringField("THUMBNAIL_QUALITY", "hqdefault.jpg")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            stringField("DEBUG_BANNER_ID", "")
        }
        debug {
            stringField("DEBUG_BANNER_ID", "ca-app-pub-3940256099942544/6300978111")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

tasks.withType<Test>().configureEach {
    reports.html.required.set(false)
    jvmArgs("-Dnet.bytebuddy.experimental=true", "-XX:+EnableDynamicAgentLoading")
}

kover {
    reports {
        filters {
            excludes {
                annotatedBy("*Preview*")
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

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
    api(libs.lucide.icons)
    implementation(libs.youtube.player)
    implementation(libs.timber)

    // Google Ads
    api(libs.play.services.ads)

    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)

    // Paging
    api(libs.paging.runtime.ktx)
    api(libs.paging.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
}

private fun VariantDimension.stringField(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}
