package br.dev.singular.overview.ui.navigation

import androidx.navigation.NavController
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

class NavigationWrapper(
    private val nav: NavController
) : INavigationWrapper {

    override fun navigate(route: String) {
        nav.navigate(route)
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
}
