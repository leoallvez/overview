package br.dev.singular.overview.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
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
import br.dev.singular.overview.util.backstack
import br.dev.singular.overview.util.getApiId
import br.dev.singular.overview.util.getParams

@Composable
fun NavController(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(bottom = 20.dp)
    ) {
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(navigate = SplashNavigate(navController))
        }
        composable(route = ScreenNav.SelectStreaming.route) {
            SelectStreamingScreen(navigate = SelectStreamingNavigate(navController))
        }
        composable(route = ScreenNav.Search.route) {
            SearchScreen(navigate = BasicNavigate(navController))
        }
        mediaDetailsGraph(navController = navController)
    }
}

fun NavGraphBuilder.mediaDetailsGraph(
    navController: NavHostController
) {
    composable(
        route = ScreenNav.MediaDetails.route,
        arguments = listOf(NavArgument.ID, NavArgument.TYPE, NavArgument.BACKSTACK)
    ) { navBackStackEntry ->
        val navigate = MediaDetailsNavigate(
            nav = navController,
            backstack = navBackStackEntry.backstack()
        )
        MediaDetailsScreen(
            params = navBackStackEntry.getParams(),
            navigate = navigate
        )
    }
    composable(
        route = ScreenNav.PersonDetails.route,
        arguments = listOf(NavArgument.ID)
    ) { navBackStackEntry ->
        PersonDetailsScreen(
            apiId = navBackStackEntry.getApiId(),
            navigate = BasicNavigate(navController, backstack = true)
        )
    }
    composable(route = ScreenNav.ExploreStreaming.route) {
        ExploreStreamingScreen(
            navigate = ExploreStreamingNavigate(navController)
        )
    }
    composable(route = ScreenNav.Liked.route) {
        LikedScreen(navigate = BasicNavigate(navController))
    }
}
