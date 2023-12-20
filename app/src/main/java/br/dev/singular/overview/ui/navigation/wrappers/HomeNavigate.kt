package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class HomeNavigate(private val navigation: NavController) {

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)

    fun toStreamingExplore(json: String) {
        navigation.navigate(route = ScreenNav.StreamingExplore.editRoute(json)) {
            popUpTo(ScreenNav.StreamingExplore.route) { inclusive = true }
        }
    }
}
