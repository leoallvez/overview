package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
interface IUiBottomNavigationState {
    val currentRoute: String?
        @Composable get
    val visible: Boolean
        @Composable get
    val navItems: PersistentList<UiBottomNavItem>
    fun onSelectItem(item: UiBottomNavItem)
}

class UiBottomNavigationState(
    private val navigation: INavigationWrapper? = null,
) : IUiBottomNavigationState {

    private var _currentRoute by mutableStateOf<String?>(UiBottomNavItem.Home.destination.route)

    override val currentRoute: String?
        @Composable
        get() = navigation?.getCurrentRoute() ?: _currentRoute

    override val visible: Boolean
        @Composable
        get() = currentRoute?.let { route ->
            hiddenRoutes.none { hidden ->
                val baseRoute = hidden.substringBefore("{")
                route.startsWith(baseRoute)
            }
        } ?: true

    private val hiddenRoutes = setOf(
        Destination.Splash.route,
        Destination.YouTubePlayer.route,
    )

    override val navItems = persistentListOf(
        UiBottomNavItem.Home,
        UiBottomNavItem.Search,
        UiBottomNavItem.Favorites,
    )

    override fun onSelectItem(item: UiBottomNavItem) {
        if (navigation == null) {
            _currentRoute = item.destination.route
            return
        }

        if (navigation.activeRoute == item.destination.route) return
        navigation.let { nav ->
            nav.navigate(item.destination.route) {
                popUpTo(nav.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

@Composable
fun rememberUiBottomNavigationState(
    navigation: INavigationWrapper? = null
): IUiBottomNavigationState = remember {
    UiBottomNavigationState(navigation)
}
