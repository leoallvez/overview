package br.com.deepbyte.take.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import br.com.deepbyte.take.Logger
import br.com.deepbyte.take.ui.ScreenNav
import br.com.deepbyte.take.ui.discover.genre.GenreDiscoverScreen
import br.com.deepbyte.take.ui.discover.provider.ProviderDiscoverScreen
import br.com.deepbyte.take.ui.home.HomeScreen
import br.com.deepbyte.take.ui.mediadetails.MediaDetailsScreen
import br.com.deepbyte.take.ui.person.CastDetailsScreen
import br.com.deepbyte.take.ui.splash.SplashScreen
import br.com.deepbyte.take.util.MediaItemClick
import br.com.deepbyte.take.util.getApiId
import br.com.deepbyte.take.util.getDiscoverParams
import br.com.deepbyte.take.util.getParams

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
    composable(
        route = ScreenNav.ProviderDiscover.route,
        arguments = listOf(
            navArgument(name = ScreenNav.JSON_PARAM) { type = NavType.StringType },
        )
    ) { navBackStackEntry ->
        ProviderDiscoverScreen(
            logger = logger,
            params = navBackStackEntry.getDiscoverParams(),
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
    composable(
        route = ScreenNav.GenreDiscover.route,
        arguments = listOf(
            navArgument(name = ScreenNav.JSON_PARAM) { type = NavType.StringType },
        )
    ) { navBackStackEntry ->
        GenreDiscoverScreen(
            logger = logger,
            params = navBackStackEntry.getDiscoverParams(),
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
}
