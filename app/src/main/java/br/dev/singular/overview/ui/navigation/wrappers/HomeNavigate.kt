package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class HomeNavigate(private val nav: NavController) : BasicNavigate(nav) {
    fun toSelectStreaming() = nav.navigate(route = ScreenNav.SelectStreaming.route)
}
