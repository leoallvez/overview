package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class SplashNavigate(private val nav: NavController) {
    fun toExploreStreaming() {
        nav.navigate(ScreenNav.ExploreStreaming.route) {
            popUpTo(ScreenNav.Splash.route) {
                inclusive = true
            }
        }
    }
}
