package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class StreamingExploreNavigate(
    private val navigation: NavController
) : BasicNavigate(navigation, backToHome = false) {

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)

    fun toHome() = navigation.navigate(route = ScreenNav.Home.route)
}
