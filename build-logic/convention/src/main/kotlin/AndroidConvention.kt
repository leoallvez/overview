import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.appId: String
    get() = requiredProperty("app.id")

/** Reads a property declared in `gradle.properties` (or passed with `-P`). */
internal fun Project.requiredProperty(name: String): String =
    providers.gradleProperty(name).orNull
        ?: error("Missing '$name' in gradle.properties")

/** Reads a version declared in `gradle/libs.versions.toml`. */
internal fun Project.catalogVersion(alias: String): String =
    extensions.getByType<VersionCatalogsExtension>()
        .named("libs")
        .findVersion(alias)
        .orElseThrow { error("Missing version '$alias' in libs.versions.toml") }
        .requiredVersion

/** Settings shared by the application and library modules. */
internal fun Project.configureAndroid(extension: CommonExtension) {
    extension.compileSdk = catalogVersion("sdk-compile").toInt()
    extension.defaultConfig.minSdk = catalogVersion("sdk-min").toInt()
    extension.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    extension.compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    extension.compileOptions.targetCompatibility = JavaVersion.VERSION_17
}
