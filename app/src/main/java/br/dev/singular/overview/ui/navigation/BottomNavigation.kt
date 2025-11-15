package br.dev.singular.overview.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.params.TagBottomNavigation
import br.dev.singular.overview.ui.ScreenNav

sealed class BottomNavigation(
    val nav: ScreenNav,
    @param:StringRes val title: Int,
    val icon: ImageVector,
    val tagDetail: String
) {

    object Home : BottomNavigation(
        nav = ScreenNav.Home,
        title = R.string.home,
        icon = Icons.Default.Home,
        tagDetail = TagBottomNavigation.Detail.HOME
    )

    object Search : BottomNavigation(
        nav = ScreenNav.Search,
        title = R.string.search,
        icon = Icons.Default.Search,
        tagDetail = TagBottomNavigation.Detail.SEARCH
    )

    object Favorites : BottomNavigation(
        nav = ScreenNav.Favorites,
        title = R.string.favorites,
        icon = Icons.Default.Favorite,
        tagDetail = TagBottomNavigation.Detail.FAVORITES
    )

    companion object {
        val items = listOf(Home, Search, Favorites)
    }
}
