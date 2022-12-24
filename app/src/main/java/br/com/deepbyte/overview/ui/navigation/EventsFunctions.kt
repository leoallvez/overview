package br.com.deepbyte.overview.ui.navigation

import androidx.navigation.NavController
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.util.MediaItemClick

fun onNavigateToMediaDetails(navigation: NavController): MediaItemClick = { id, type ->
    navigation.navigate(ScreenNav.MediaDetails.editRoute(id, type))
}

fun onNavigateToHome(navigation: NavController): () -> Unit = {
    navigation.navigate(route = ScreenNav.Home.route) {
        popUpTo(ScreenNav.Splash.route) {
            inclusive = true
        }
    }
}
