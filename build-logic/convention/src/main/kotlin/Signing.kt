import com.android.build.api.dsl.ApplicationExtension
import java.io.File

/** Whether release signing is enabled through the `OVER_ACTIVE_SIGNING` environment variable. */
fun isActiveSigning() = System.getenv("OVER_ACTIVE_SIGNING") == "true"

/**
 * Creates the signing config [name] from the `OVER_<NAME>_KEYSTORE`, `OVER_<NAME>_PASSWORD`
 * and `OVER_<NAME>_KEY_ALIAS` environment variables. The keystore path is relative to the
 * user home.
 */
fun ApplicationExtension.envSigningConfig(name: String) {
    val prefix = "OVER_${name.uppercase()}"
    val home = System.getProperty("user.home")
    signingConfigs.create(name).apply {
        storeFile = File("$home${System.getenv("${prefix}_KEYSTORE")}")
        storePassword = System.getenv("${prefix}_PASSWORD")
        keyAlias = System.getenv("${prefix}_KEY_ALIAS")
        keyPassword = System.getenv("${prefix}_PASSWORD")
    }
}
