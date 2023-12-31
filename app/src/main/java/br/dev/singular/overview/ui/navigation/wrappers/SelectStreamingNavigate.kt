package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class SelectStreamingNavigate(private val navigation: NavController) : BasicNavigate(navigation) {

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)

    fun toStreamingExplore() {
        navigation.navigate(route = ScreenNav.StreamingExplore.route) {
            popUpTo(ScreenNav.StreamingExplore.route) { inclusive = true }
        }
    }
}
