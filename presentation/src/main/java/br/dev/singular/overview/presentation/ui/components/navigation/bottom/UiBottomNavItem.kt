package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.navigation.Destination
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search

sealed class UiBottomNavItem(
    val destination: Destination,
    @param:StringRes val title: Int,
    val icon: ImageVector
) {
    object Home : UiBottomNavItem(
        destination = Destination.SelectCatalog,
        title = R.string.home,
        icon = Lucide.House,
    )

    object Search : UiBottomNavItem(
        destination = Destination.Search,
        title = R.string.search,
        icon = Lucide.Search,
    )

    object Favorites : UiBottomNavItem(
        destination = Destination.Favorites,
        title = R.string.favorites,
        icon = Lucide.Heart,
    )
}
