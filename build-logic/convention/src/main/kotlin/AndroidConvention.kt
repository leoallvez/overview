import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal val Project.appId: String
    get() = requiredProperty("app.id")

/** Reads a property declared in `gradle.properties` (or passed with `-P`). */
internal fun Project.requiredProperty(name: String): String =
    providers.gradleProperty(name).orNull
        ?: error("Missing '$name' in gradle.properties")

/** Settings shared by the application and library modules. */
internal fun Project.configureAndroid(extension: CommonExtension) {
    extension.compileSdk = requiredProperty("sdk.compile").toInt()
    extension.defaultConfig.minSdk = requiredProperty("sdk.min").toInt()
    extension.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    extension.compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    extension.compileOptions.targetCompatibility = JavaVersion.VERSION_17
}
