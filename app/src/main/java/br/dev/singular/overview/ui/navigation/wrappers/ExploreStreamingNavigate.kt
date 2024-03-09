package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav

class ExploreStreamingNavigate(
    private val nav: NavController
) : BasicNavigate(nav) {

    fun toSearch() = nav.navigate(route = ScreenNav.Search.route)

    fun toLiked() = nav.navigate(route = ScreenNav.Liked.route)

    fun toSelectStreaming() = nav.navigate(route = ScreenNav.SelectStreaming.route)
}
