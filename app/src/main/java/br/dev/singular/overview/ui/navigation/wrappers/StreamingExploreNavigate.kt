package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class StreamingExploreNavigate(
    private val navigation: NavController
) : BasicNavigate(navigation, backstack = false) {

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)

    fun toSelectStreaming() = navigation.navigate(route = ScreenNav.SelectStreaming.route)
}
