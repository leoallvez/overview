import com.android.build.api.dsl.ApplicationProductFlavor

/** Declares the `app_name` string resource for the flavor. */
fun ApplicationProductFlavor.setAppName(appName: String) {
    resValue("string", "app_name", appName)
}
