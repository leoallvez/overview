package br.com.deepbyte.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav

class StreamingExploreNavigate(
    private val navigation: NavController
) : BasicNavigate(navigation, backToHome = false) {

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)
}