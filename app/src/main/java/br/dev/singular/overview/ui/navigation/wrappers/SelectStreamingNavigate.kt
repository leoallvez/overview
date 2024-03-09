package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class SelectStreamingNavigate(private val nav: NavController) : BasicNavigate(nav) {
    fun toSearch() = nav.navigate(route = ScreenNav.Search.route)
}
