package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.navigation.Destination

open class BasicNavigate(private val nav: NavController) {

    fun toMediaDetails(media: MediaUiModel) = media.apply {
        nav.navigate(Destination.MediaDetails.editRoute(id, type.name.lowercase()))
    }

    fun popBackStack() = nav.popBackStack()

    fun toHome() {
        nav.navigate(route = Destination.CatalogDetails.route) {
            popUpTo(Destination.Splash.route) {
                inclusive = true
            }
        }
    }
}
