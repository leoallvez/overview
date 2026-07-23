package br.dev.singular.overview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
class NavigationWrapper(
    private val nav: NavController,
) : INavigationWrapper {

    override val startDestinationId: Int
        get() = nav.graph.startDestinationId

    override fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit) {
        nav.navigate(route, builder)
    }

    override fun toHome() {
        nav.navigate(route = Destination.CatalogDetails.route) {
            popUpTo(Destination.Splash.route) {
                inclusive = true
            }
        }
    }

    override fun popBackStack() {
        nav.popBackStack()
    }

    override fun toMediaDetails(media: MediaUiModel) = with(media) {
        nav.navigate(Destination.MediaDetails.editRoute(id, type.name.lowercase()))
    }

    override val activeRoute: String?
        get() = nav.currentBackStackEntry?.destination?.route

    @Composable
    override fun getCurrentRoute(): String? {
        return nav.currentBackStackEntryAsState().value?.destination?.route
    }
}
