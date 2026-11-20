import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention for library modules: applies the Android library plugin and the settings
 * shared by every Android module. The namespace is the app id followed by the module name.
 * Release builds are not minified and each module ships its own `consumer-rules.pro`.
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")

        extensions.configure<LibraryExtension> {
            configureAndroid(this)
            namespace = "$appId.$name"
            defaultConfig.consumerProguardFiles("consumer-rules.pro")
            buildTypes.getByName("release").apply {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}
