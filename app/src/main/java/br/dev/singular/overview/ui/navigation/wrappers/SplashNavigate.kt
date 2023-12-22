package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

interface ISplashNavigate {
    fun toStreamingExplore(json: String)
}

class SplashNavigate(val navigation: NavController) : ISplashNavigate {
    override fun toStreamingExplore(json: String) {
        val route = ScreenNav.StreamingExplore.editRoute(json)
        navigation.navigate(route) {
            // this is necessary to avoid the splash screen to be shown when the user press back;
            popUpTo(ScreenNav.Splash.route) {
                inclusive = true
            }
        }
    }
}
