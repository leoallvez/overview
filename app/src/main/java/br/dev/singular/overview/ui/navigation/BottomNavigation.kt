package br.dev.singular.overview.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import br.dev.singular.overview.R
import br.dev.singular.overview.ui.ScreenNav

sealed class BottomNavigation(
    val nav: ScreenNav,
    @StringRes val title: Int,
    val icon: ImageVector
) {

    object Home : BottomNavigation(
        nav = ScreenNav.ExploreStreaming,
        title = R.string.home,
        icon = Icons.Default.Home
    )

    object Search : BottomNavigation(
        nav = ScreenNav.Search,
        title = R.string.search,
        icon = Icons.Default.Search
    )

    object Favorites : BottomNavigation(
        nav = ScreenNav.Liked,
        title = R.string.favorites,
        icon = Icons.Default.Favorite
    )

    companion object {
        val items = listOf(Home, Search, Favorites)
    }
}
