package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class SelectStreamingNavigate(private val nav: NavController) : BasicNavigate(nav) {

    fun toSearch() = nav.navigate(route = ScreenNav.Search.route)

    fun toExploreStreaming() {
        nav.navigate(route = ScreenNav.ExploreStreaming.route) {
            popUpTo(ScreenNav.ExploreStreaming.route) { inclusive = true }
        }
    }
}
