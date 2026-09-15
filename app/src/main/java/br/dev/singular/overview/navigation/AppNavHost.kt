package br.dev.singular.overview.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.dev.singular.overview.navigation.routes.catalogDetailsRoute
import br.dev.singular.overview.navigation.routes.changeCatalogRoute
import br.dev.singular.overview.navigation.routes.favoritesRoute
import br.dev.singular.overview.navigation.routes.mediaDetailsRoute
import br.dev.singular.overview.navigation.routes.personDetailsRoute
import br.dev.singular.overview.navigation.routes.profileRoute
import br.dev.singular.overview.navigation.routes.searchRoute
import br.dev.singular.overview.navigation.routes.selectCatalogRoute
import br.dev.singular.overview.navigation.routes.selectGenreRoute
import br.dev.singular.overview.navigation.routes.splashRoute
import br.dev.singular.overview.navigation.routes.youTubePlayerRoute
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHostState
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.theme.Background

@Composable
fun AppNavHost(
    navController: NavHostController,
    showAds: Boolean,
    snackbarHostState: UiSnackbarHostState,
    modifier: Modifier,
    setEdgeToEdge: (Boolean) -> Unit,
) {
    val navi: INavigationWrapper = remember(navController) {
        NavigationWrapper(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Splash.route,
        modifier = modifier.background(Background)
    ) {
        splashRoute(navi)
        selectCatalogRoute(navi)
        changeCatalogRoute(navi)
        selectGenreRoute(navi)
        catalogDetailsRoute(navi)
        searchRoute(navi)
        mediaDetailsRoute(navi, showAds, snackbarHostState)
        youTubePlayerRoute(navi, setEdgeToEdge)
        personDetailsRoute(navi, showAds)
        favoritesRoute(navi, snackbarHostState)
        profileRoute(navi)
    }
}
