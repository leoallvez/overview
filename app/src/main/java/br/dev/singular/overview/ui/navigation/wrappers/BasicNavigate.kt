package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

open class BasicNavigate(private val nav: NavController) {

    // TODO: remove long press parameter.
    fun toMediaDetails(apiId: Long, mediaType: String?) {
        nav.navigate(ScreenNav.MediaDetails.editRoute(apiId, mediaType))
    }
    fun popBackStack() = nav.popBackStack()

    // TODO: user this with long press.
    // https://stackoverflow.com/questions/76395349/jetpack-compose-android-button-detect-long-click
    fun toExploreStreaming() {
        nav.navigate(route = ScreenNav.ExploreStreaming.route) {
            popUpTo(ScreenNav.Splash.route) {
                inclusive = true
            }
        }
    }
}
