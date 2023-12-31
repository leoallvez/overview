package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

interface ISplashNavigate {
    fun toStreamingExplore()
}

class SplashNavigate(val navigation: NavController) : ISplashNavigate {
    override fun toStreamingExplore() {
        navigation.navigate(ScreenNav.StreamingExplore.route) {
            popUpTo(ScreenNav.Splash.route) {
                inclusive = true
            }
        }
    }
}
