package br.com.deepbyte.overview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.discover.GenreDiscoverScreen
import br.com.deepbyte.overview.ui.discover.ProviderDiscoverScreen
import br.com.deepbyte.overview.ui.home.HomeScreen
import br.com.deepbyte.overview.ui.media.MediaDetailsScreen
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.navigation.events.MediaDetailsScreenEvents
import br.com.deepbyte.overview.ui.person.CastDetailsScreen
import br.com.deepbyte.overview.ui.search.SearchScreen
import br.com.deepbyte.overview.ui.splash.SplashScreen
import br.com.deepbyte.overview.util.getApiId
import br.com.deepbyte.overview.util.getDiscoverParams
import br.com.deepbyte.overview.util.getParams
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@Composable
fun NavController(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ScreenNav.Splash.route
    ) {
        composable(route = ScreenNav.Splash.route) {
            SplashScreen(onNavigateToHome = onNavigateToHome(navController))
        }
        composable(route = ScreenNav.Home.route) {
            HomeScreen(
                onNavigateToMediaDetails = onNavigateToMediaDetails(navController),
                onNavigateToSearch = {
                    navController.navigate(route = ScreenNav.Search.route)
                }
            )
        }
        composable(route = ScreenNav.Search.route) {
            SearchScreen(events = BasicsMediaEvents(navController))
        }
        mediaDetailsGraph(navController = navController)
    }
}

fun NavGraphBuilder.mediaDetailsGraph(
    navController: NavHostController
) {
    composable(
        route = ScreenNav.MediaDetails.route,
        arguments = listOf(NavArgument.ID, NavArgument.TYPE)
    ) { navBackStackEntry ->
        MediaDetailsScreen(
            params = navBackStackEntry.getParams(),
            events = MediaDetailsScreenEvents(navController)
        )
    }
    composable(
        route = ScreenNav.CastDetails.route,
        arguments = listOf(NavArgument.ID)
    ) { navBackStackEntry ->
        CastDetailsScreen(
            apiId = navBackStackEntry.getApiId(),
            events = BasicsMediaEvents(navController)
        )
    }
    composable(
        route = ScreenNav.ProviderDiscover.route,
        arguments = listOf(NavArgument.ID)
    ) { navBackStackEntry ->
        ProviderDiscoverScreen(
            params = navBackStackEntry.getDiscoverParams(),
            onNavigateToMediaDetails = onNavigateToMediaDetails(navController)
        )
    }
    composable(
        route = ScreenNav.GenreDiscover.route,
        arguments = listOf(NavArgument.JSON)
    ) { navBackStackEntry ->
        GenreDiscoverScreen(
            params = navBackStackEntry.getDiscoverParams(),
            onNavigateToMediaDetails = onNavigateToMediaDetails(navController)
        )
    }
}
