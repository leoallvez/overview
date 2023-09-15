package dev.com.singular.overview.ui.navigation

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
import dev.com.singular.overview.ui.ScreenNav
import dev.com.singular.overview.ui.media.MediaDetailsScreen
import dev.com.singular.overview.ui.person.CastDetailsScreen
import dev.com.singular.overview.ui.search.SearchScreen
import dev.com.singular.overview.ui.splash.SplashScreen
import dev.com.singular.overview.ui.streaming.StreamingExploreScreen
import dev.com.singular.overview.ui.streaming.StreamingOverviewEditScreen
import dev.com.singular.overview.ui.theme.PrimaryBackground
import dev.com.singular.overview.util.getApiId
import dev.com.singular.overview.util.getBackToHome
import dev.com.singular.overview.util.getParams
import dev.com.singular.overview.util.getStreamingParams
import dev.com.singular.overview.ui.home.HomeScreen
import dev.com.singular.overview.ui.navigation.args.StreamingArgType
import dev.com.singular.overview.ui.navigation.wrappers.BasicNavigate
import dev.com.singular.overview.ui.navigation.wrappers.HomeNavigate
import dev.com.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import dev.com.singular.overview.ui.navigation.wrappers.StreamingExploreNavigate

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
    composable(
        route = ScreenNav.StreamingExploreEdit.route
    ) {
        StreamingOverviewEditScreen()
    }
}
