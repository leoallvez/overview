package dev.com.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import dev.com.singular.overview.ui.ScreenNav

class StreamingExploreNavigate(
    private val navigation: NavController
) : BasicNavigate(navigation, backToHome = false) {

    fun toSearch() = navigation.navigate(route = ScreenNav.Search.route)
}
