import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("overview.android.application")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kover)
}

extensions.configure<ApplicationExtension> {
    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    if (isActiveSigning()) {
        envSigningConfig("prd")
        envSigningConfig("hmg")
    }

    flavorDimensions.add("version")
    productFlavors {
        create("dev") {
            setAppName("OVER.DEV")
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("hmg") {
            setAppName("OVER.HMG")
            dimension = "version"
            applicationIdSuffix = ".homol"
            versionNameSuffix = "-hmg"
            if (isActiveSigning()) {
                signingConfig = signingConfigs.getByName("hmg")
            }
        }
        create("prd") {
            setAppName("Overview")
            dimension = "version"
            if (isActiveSigning()) {
                signingConfig = signingConfigs.getByName("prd")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    @Suppress("UnstableApiUsage")
    androidResources {
        localeFilters += listOf(
            "en", "de", "es", "es-rUS", "fr", "in", "it", "ja", "ko", "pt-rBR", "th", "tr", "zh-rCN"
        )
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

dependencies {
    implementation(libs.androidx.core.ktx)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)

    // Third-party libraries
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.serialization)
    implementation(libs.googleid)

    // Modules
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
}
