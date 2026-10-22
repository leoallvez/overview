package br.dev.singular.overview.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.splash.SplashScreen

fun NavGraphBuilder.splashRoute(
    navigation: INavigationWrapper
) {
    composable(route = Destination.Splash.route) {
        SplashScreen(
            onToHome = {
                navigation.navigate(route = Destination.SelectCatalog.route) {
                    popUpTo(Destination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
