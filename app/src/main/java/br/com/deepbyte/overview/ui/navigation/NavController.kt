package br.com.deepbyte.overview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.discover.genre.GenreDiscoverScreen
import br.com.deepbyte.overview.ui.discover.provider.ProviderDiscoverScreen
import br.com.deepbyte.overview.ui.home.HomeScreen
import br.com.deepbyte.overview.ui.media.MediaDetailsScreen
import br.com.deepbyte.overview.ui.person.CastDetailsScreen
import br.com.deepbyte.overview.ui.splash.SplashScreen
import br.com.deepbyte.overview.util.MediaItemClick
import br.com.deepbyte.overview.util.getApiId
import br.com.deepbyte.overview.util.getDiscoverParams
import br.com.deepbyte.overview.util.getParams
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@Composable
fun NavController(navController: NavHostController = rememberNavController()) {

    val onNavigateToMediaDetails: MediaItemClick = { id, type ->
        navController.navigate(ScreenNav.MediaDetails.editRoute(id, type))
    }
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route
    ) {
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(
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
                onNavigateToMediaDetails = onNavigateToMediaDetails
            )
        }
        mediaDetailsGraph(
            navController = navController,
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
}

fun NavGraphBuilder.mediaDetailsGraph(
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
            onNavigateToHome = { navController.navigate(ScreenNav.Home.route) },
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
    composable(
        route = ScreenNav.ProviderDiscover.route,
        arguments = listOf(
            navArgument(name = ScreenNav.JSON_PARAM) { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        ProviderDiscoverScreen(
            params = navBackStackEntry.getDiscoverParams(),
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
    composable(
        route = ScreenNav.GenreDiscover.route,
        arguments = listOf(
            navArgument(name = ScreenNav.JSON_PARAM) { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        GenreDiscoverScreen(
            params = navBackStackEntry.getDiscoverParams(),
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
}
