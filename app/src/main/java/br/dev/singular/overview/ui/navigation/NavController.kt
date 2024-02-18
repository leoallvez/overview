package br.dev.singular.overview.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.liked.LikedScreen
import br.dev.singular.overview.ui.media.MediaDetailsScreen
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.navigation.wrappers.ExploreStreamingNavigate
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.ui.navigation.wrappers.SelectStreamingNavigate
import br.dev.singular.overview.ui.navigation.wrappers.SplashNavigate
import br.dev.singular.overview.ui.person.PersonDetailsScreen
import br.dev.singular.overview.ui.search.SearchScreen
import br.dev.singular.overview.ui.splash.SplashScreen
import br.dev.singular.overview.ui.streaming.explore.ExploreStreamingScreen
import br.dev.singular.overview.ui.streaming.select.SelectStreamingScreen
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.getApiId
import br.dev.singular.overview.util.getParams

@Composable
fun NavController(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route,
        modifier = Modifier.background(PrimaryBackground)
    ) {
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(navigate = SplashNavigate(navController))
        }
        composable(
            route = ScreenNav.SelectStreaming.route,
            enterTransition = { onInAnimation() },
            exitTransition = { onOutAnimation() }
        ) {
            SelectStreamingScreen(navigate = SelectStreamingNavigate(navController))
        }
        composable(route = ScreenNav.Search.route) {
            SearchScreen(navigate = BasicNavigate(navController))
        }
        composable(
            route = ScreenNav.MediaDetails.route,
            arguments = listOf(NavArg.ID, NavArg.TYPE, NavArg.BACKSTACK)
        ) { navBackStackEntry ->
            MediaDetailsScreen(
                params = navBackStackEntry.getParams(),
                navigate = MediaDetailsNavigate(nav = navController)
            )
        }
        composable(
            route = ScreenNav.PersonDetails.route,
            arguments = listOf(NavArg.ID)
        ) { navBackStackEntry ->
            PersonDetailsScreen(
                apiId = navBackStackEntry.getApiId(),
                navigate = BasicNavigate(nav = navController)
            )
        }
        composable(route = ScreenNav.ExploreStreaming.route) {
            ExploreStreamingScreen(
                navigate = ExploreStreamingNavigate(nav = navController)
            )
        }
        composable(route = ScreenNav.Liked.route) {
            LikedScreen(navigate = BasicNavigate(nav = navController))
        }
    }
}

fun onInAnimation() = slideInHorizontally(animationSpec = tween(ANIMATION_DURATION_MILLIS))
fun onOutAnimation() = slideOutHorizontally(animationSpec = tween(ANIMATION_DURATION_MILLIS))

const val ANIMATION_DURATION_MILLIS = 450
