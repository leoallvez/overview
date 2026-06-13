plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kover)
    alias(libs.plugins.pitest)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

pitest {
    targetClasses.set(listOf("br.dev.singular.overview.domain.*"))
    targetTests.set(listOf("br.dev.singular.overview.domain.*"))
    excludedClasses.set(
        listOf(
            "br.dev.singular.overview.domain.model.*",
            "br.dev.singular.overview.domain.repository.*"
        )
    )
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(listOf("HTML", "XML"))
    timestampedReports.set(false)
    excludedMethods.set(listOf("equals", "hashCode", "toString", "copy", "component1", "component2", "component3", "component4"))

    mutationThreshold.set(50)
    testStrengthThreshold.set(70)
}
