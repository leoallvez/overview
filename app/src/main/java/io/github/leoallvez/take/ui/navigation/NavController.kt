package io.github.leoallvez.take.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.Screen
import io.github.leoallvez.take.ui.cast_details.CastDetailsScreen
import io.github.leoallvez.take.ui.discover.DiscoverScreen
import io.github.leoallvez.take.ui.home.HomeScreen
import io.github.leoallvez.take.ui.media_details.MediaDetailsScreen
import io.github.leoallvez.take.ui.splash.SplashScreen
import io.github.leoallvez.take.util.getParams

@ExperimentalPagerApi
@Composable
fun NavController(logger: Logger, navController: NavHostController = rememberNavController()) {

    val onNavigateToMediaDetails = { apiId: Long, mediaType: String? ->
        navController.navigate(
            Screen.MediaDetails.editRoute(id = apiId, type = mediaType)
        )
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                logger = logger,
                onNavigateToHome = {
                    navController.navigate(route = Screen.Home.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screen.Home.route) {
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
            route = Screen.Discover.route,
            arguments = listOf(
                navArgument(name = Screen.ID_PARAM) { type = NavType.LongType}
            )
        ) {
            DiscoverScreen()
        }
    }
}

fun NavGraphBuilder.mediaDetailsGraph(
    logger: Logger,
    navController: NavHostController,
    onNavigateToMediaDetails: (Long, String?) -> Unit
) {
    composable(
        route = Screen.MediaDetails.route,
        arguments = listOf(
            navArgument(name = Screen.ID_PARAM) { type = NavType.LongType },
            navArgument(name = Screen.TYPE_PARAM) { type = NavType.StringType },
        )
    ) { navBackStackEntry ->
        MediaDetailsScreen(
            logger = logger,
            params = navBackStackEntry.getParams(),
            events = MediaDetailsScreenEvents(navController),
        )
    }
    composable(
        route = Screen.CastDetails.route,
        arguments = listOf(
            navArgument(name = Screen.ID_PARAM) { type = NavType.LongType}
        )
    ) { navBackStackEntry ->
        CastDetailsScreen(
            apiId = navBackStackEntry.arguments?.getLong(Screen.ID_PARAM),
            logger = logger,
            onNavigateToHome = { navController.navigate(Screen.Home.route) },
            onNavigateToMediaDetails = onNavigateToMediaDetails,
        )
    }
}
