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
import androidx.navigation.navArgument
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.home.HomeScreen
import br.dev.singular.overview.ui.media.MediaDetailsScreen
import br.dev.singular.overview.ui.navigation.args.StreamingArgType
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.navigation.wrappers.HomeNavigate
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.ui.navigation.wrappers.StreamingExploreNavigate
import br.dev.singular.overview.ui.person.CastDetailsScreen
import br.dev.singular.overview.ui.search.SearchScreen
import br.dev.singular.overview.ui.splash.SplashScreen
import br.dev.singular.overview.ui.streaming.StreamingExploreScreen
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.getApiId
import br.dev.singular.overview.util.getBackToHome
import br.dev.singular.overview.util.getParams
import br.dev.singular.overview.util.getStreamingParams

@Composable
fun NavController(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route,
        modifier = Modifier.background(PrimaryBackground).padding(bottom = 20.dp)
    ) {
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(onNavigateToHome = onNavigateToHome(navController))
        }
        composable(route = ScreenNav.Home.route) {
            HomeScreen(navigate = HomeNavigate(navController))
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
        arguments = listOf(NavArgument.ID, NavArgument.TYPE, NavArgument.BACK_TO_HOME)
    ) { navBackStackEntry ->
        val navigate = MediaDetailsNavigate(
            navigation = navController,
            backToHome = navBackStackEntry.getBackToHome()
        )
        MediaDetailsScreen(
            params = navBackStackEntry.getParams(),
            navigate = navigate
        )
    }
    composable(
        route = ScreenNav.CastDetails.route,
        arguments = listOf(NavArgument.ID)
    ) { navBackStackEntry ->
        CastDetailsScreen(
            apiId = navBackStackEntry.getApiId(),
            navigate = BasicNavigate(navController, backToHome = true)
        )
    }
    composable(
        route = ScreenNav.StreamingExplore.route,
        arguments = listOf(navArgument(ScreenNav.JSON_PARAM) { type = StreamingArgType() })
    ) { navBackStackEntry ->
        StreamingExploreScreen(
            streaming = navBackStackEntry.getStreamingParams(),
            navigate = StreamingExploreNavigate(navController)
        )
    }
}
