package io.github.leoallvez.take.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.ScreenNav
import io.github.leoallvez.take.ui.discover.DiscoverScreen
import io.github.leoallvez.take.ui.home.HomeScreen
import io.github.leoallvez.take.ui.media_details.MediaDetailsScreen
import io.github.leoallvez.take.ui.person.CastDetailsScreen
import io.github.leoallvez.take.ui.splash.SplashScreen
import io.github.leoallvez.take.util.MediaItemClick
import io.github.leoallvez.take.util.getApiId
import io.github.leoallvez.take.util.getParams

@ExperimentalPagerApi
@Composable
fun NavController(logger: Logger, navController: NavHostController = rememberNavController()) {

    val onNavigateToMediaDetails: MediaItemClick = { id, type ->
        navController.navigate(ScreenNav.MediaDetails.editRoute(id, type))
    }
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route
    ) {
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(
                logger = logger,
                onNavigateToHome = {
                    navController.navigate(route = ScreenNav.Home.route) {
                        popUpTo(ScreenNav.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = ScreenNav.Home.route) {
            HomeScreen(
                logger = logger,
                onNavigateToMediaDetails = onNavigateToMediaDetails
            )
        }
        mediaDetailsGraph(
            logger = logger,
            navController = navController,
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
        composable(
            route = ScreenNav.Discover.route,
            arguments = listOf(
                navArgument(name = ScreenNav.ID_PARAM) { type = NavType.LongType }
            )
        ) {
            DiscoverScreen()
        }
    }
}

fun NavGraphBuilder.mediaDetailsGraph(
    logger: Logger,
    navController: NavHostController,
    onNavigateToMediaDetails: MediaItemClick
) {
    composable(
        route = ScreenNav.MediaDetails.route,
        arguments = listOf(
            navArgument(name = ScreenNav.ID_PARAM) { type = NavType.LongType },
            navArgument(name = ScreenNav.TYPE_PARAM) { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        MediaDetailsScreen(
            logger = logger,
            params = navBackStackEntry.getParams(),
            events = MediaDetailsScreenEvents(navController)
        )
    }
    composable(
        route = ScreenNav.CastDetails.route,
        arguments = listOf(
            navArgument(name = ScreenNav.ID_PARAM) { type = NavType.LongType }
        )
    ) { navBackStackEntry ->
        CastDetailsScreen(
            apiId = navBackStackEntry.getApiId(),
            logger = logger,
            onNavigateToHome = { navController.navigate(ScreenNav.Home.route) },
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
}
