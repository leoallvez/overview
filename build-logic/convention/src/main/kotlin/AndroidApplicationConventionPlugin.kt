import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention for the application module: applies the Android application plugin and
 * the settings shared by every Android module, plus the app identity and version.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension> {
            configureAndroid(this)
            namespace = appId
            defaultConfig.applicationId = appId
            defaultConfig.targetSdk = catalogVersion("sdk-target").toInt()
            defaultConfig.versionCode = requiredProperty("app.versionCode").toInt()
            defaultConfig.versionName = requiredProperty("app.versionName")
        }
    }
}
