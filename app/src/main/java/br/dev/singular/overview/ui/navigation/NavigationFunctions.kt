package br.dev.singular.overview.ui.navigation

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.util.MediaItemClick

fun onNavigateToMediaDetails(
    navigate: NavController,
    backToHome: Boolean = false
): MediaItemClick = { id, type ->
    navigate.navigate(ScreenNav.MediaDetails.editRoute(id, type, backToHome))
}

fun onNavigateToHome(navigate: NavController): () -> Unit = {
    navigate.navigate(route = ScreenNav.Home.route) {
        popUpTo(ScreenNav.Splash.route) {
            inclusive = true
        }
    }
}
