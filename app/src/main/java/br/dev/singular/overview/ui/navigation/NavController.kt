package br.dev.singular.overview.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.dev.singular.overview.presentation.ui.screens.favorites.FavoritesScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.media.MediaDetailsScreen
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.navigation.wrappers.HomeNavigate
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.presentation.ui.screens.person.PersonDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.person.PersonDetailsViewModel
import br.dev.singular.overview.ui.search.SearchScreen
import br.dev.singular.overview.presentation.ui.screens.splash.SplashScreen
import br.dev.singular.overview.ui.home.HomeScreen
import br.dev.singular.overview.ui.streaming.select.SelectStreamingScreen
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.video.YouTubePlayerFullscreen
import br.dev.singular.overview.util.getApiId
import br.dev.singular.overview.util.getParams

@Composable
fun NavController(
    showAds: Boolean,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route,
        modifier = Modifier.background(PrimaryBackground)
    ) {
        val basicNav = BasicNavigate(navController)
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(onToHome = { basicNav.toHome() })
        }
        composable(
            route = ScreenNav.SelectStreaming.route,
            exitTransition = { upExitTransition(duration = AnimationDurations.LONG) },
            enterTransition = { downEnterTransition(duration = AnimationDurations.LONG) }
        ) {
            SelectStreamingScreen(
                onBack = { basicNav.popBackStack() },
                onToHome = { basicNav.toHome() }
            )
        }
        composable(route = ScreenNav.Search.route) {
            SearchScreen(navigate = basicNav)
        }
        composable(
            route = ScreenNav.MediaDetails.route,
            arguments = listOf(NavArg.ID, NavArg.TYPE, NavArg.BACKSTACK),
            exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
        ) { navBackStackEntry ->
            MediaDetailsScreen(
                params = navBackStackEntry.getParams(),
                navigate = MediaDetailsNavigate(nav = navController)
            )
        }
        composable(
            route = ScreenNav.YouTubePlayer.route,
            arguments = listOf(
                navArgument(name = ScreenNav.VIDEO_KEY_PARAM) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            YouTubePlayerFullscreen(
                videoKey = backStackEntry.arguments?.getString(ScreenNav.VIDEO_KEY_PARAM) ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ScreenNav.PersonDetails.route,
            arguments = listOf(NavArg.ID),
            exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
        ) { navBackStackEntry ->

            val viewModel = hiltViewModel<PersonDetailsViewModel>()

            PersonDetailsScreen(
                uiState = viewModel.uiState.collectAsState().value,
                showAds = showAds,
                onLoad = { viewModel.onLoad(id = navBackStackEntry.getApiId()) },
                onBack = { navController.popBackStack() },
                onToMediaDetails = { basicNav.toMediaDetails(it) }
            )
        }
        composable(route = ScreenNav.Home.route) {
            HomeScreen(
                navigate = HomeNavigate(nav = navController)
            )
        }
        composable(route = ScreenNav.Favorites.route) {
            FavoritesScreen(onToMediaDetails = { basicNav.toMediaDetails(it) })
        }
    }
}
