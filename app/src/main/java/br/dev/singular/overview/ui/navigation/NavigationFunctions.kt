package br.dev.singular.overview.ui.navigation

import androidx.navigation.NavController
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.util.MediaItemClick

fun onNavigateToMediaDetails(
    navigate: NavController,
    backstack: Boolean = false
): MediaItemClick = { id, type ->
    navigate.navigate(ScreenNav.MediaDetails.editRoute(id, type, backstack))
}

fun onBackstack(navigate: NavController): () -> Unit = {
    navigate.navigate(route = ScreenNav.SelectStreaming.route) {
        // this is necessary to avoid the splash screen to be shown when the user press back;
        popUpTo(ScreenNav.Splash.route) {
            inclusive = true
        }
    }
}
