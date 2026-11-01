import com.android.build.api.dsl.LibraryExtension

plugins {
    id("overview.android.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.plugin)
}

extensions.configure<LibraryExtension> {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
        stringField("TMDB_API_URL", "https://api.themoviedb.org/3/")
        stringField("TMDB_API_KEY", System.getenv("OVER_TMDB_API_KEY") ?: "")
        stringField("JEV_API_URL", "https://api.typesafe.ai/")
        stringField("JEV_API_KEY", System.getenv("OVER_JEV_API_KEY") ?: "")
        stringField("JEV_MODEL", "jev-1.13.0")
        intField("PAGE_SIZE", 20)
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
            longField("REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS", 3600)
        }
        debug {
            longField("REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS", 0)
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*.BuildConfig",
                    "*.*_Factory*",
                    "*.*_HiltModules*",
                    "*.*_Impl*",
                    "*.Hilt_*",
                    "**.*_Provide*Factory*",
                    "**.database.AppDatabase*",
                    "**.database.Migration*",
                    "**.database.dao.*",
                    $$"*.$serializer",
                    $$"**.$serializer",
                    $$"**.*$serializer",
                    "**.*_MembersInjector*",
                )
            }
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    api(libs.retrofit)

    // Room
    api(libs.room.runtime)
    api(libs.room.ktx)
    api(libs.room.paging)
    ksp(libs.room.compiler)

    // DataStore
    api(libs.datastore.preferences)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    api(libs.kotlinx.serialization.json)
    api(libs.network.response.adapter)

    api(platform(libs.firebase.bom))
    api(libs.firebase.config)
    api(libs.firebase.auth)
    api(libs.firebase.firestore)
    implementation(libs.kotlinx.coroutines.play.services)

    // Credential Manager
    api(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    api(libs.timber)

    implementation(project(":domain"))

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
