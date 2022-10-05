package io.github.leoallvez.take.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.cast_details.CastDetailsScreen
import io.github.leoallvez.take.ui.discover.DiscoverScreen
import io.github.leoallvez.take.ui.home.HomeScreen
import io.github.leoallvez.take.ui.media_details.MediaDetailsScreen
import io.github.leoallvez.take.ui.splash.SplashScreen
import io.github.leoallvez.take.util.getParams

@ExperimentalPagerApi
@Composable
fun NavController(logger: Logger) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(nav = navController,logger = logger)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(nav = navController, logger = logger)
        }
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
                nav = navController,
                logger = logger,
            )
        }
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

class MediaDetailsScreenEvents(private val navigation: NavController) {

    fun onNavigateToHome()
        = navigation.navigate(Screen.Home.route)

    fun onNavigateToDiscover(apiId: Long)
        = navigation.navigate(Screen.Discover.editRoute(apiId))

    fun onNavigateToCastDetails(apiId: Long)
        = navigation.navigate(Screen.CastDetails.editRoute(apiId))

    fun onNavigateToMediaDetails(apiId: Long, mediaType: String?) {
        navigation.navigate(
            Screen.MediaDetails.editRoute(id = apiId, type = mediaType)
        )
    }
}
