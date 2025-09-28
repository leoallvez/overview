package br.dev.singular.overview.ui.navigation.wrappers

import androidx.navigation.NavController
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.ui.ScreenNav

open class BasicNavigate(private val nav: NavController) {

    fun toMediaDetails(media: MediaUiModel) = media.apply {
        nav.navigate(ScreenNav.MediaDetails.editRoute(id, type.name.lowercase()))
    }

    fun popBackStack() = nav.popBackStack()

    fun toHome() {
        nav.navigate(route = ScreenNav.Home.route) {
            popUpTo(ScreenNav.Splash.route) {
                inclusive = true
            }
        }
    }
}
