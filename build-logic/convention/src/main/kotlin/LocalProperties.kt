import org.gradle.api.Project
import java.util.Properties

/**
 * Reads a flag override declared in `local.properties`.
 * Returns `true` for `on`, `false` for `off` and `null` when it is absent or has any other value.
 */
fun Project.localFlag(name: String): Boolean? {
    val file = rootProject.layout.projectDirectory.file("local.properties")
    val content = providers.fileContents(file).asText.orNull ?: return null
    val properties = Properties().apply { load(content.reader()) }
    return when (properties.getProperty(name)?.trim()?.lowercase()) {
        "on" -> true
        "off" -> false
        else -> null
    }
}
