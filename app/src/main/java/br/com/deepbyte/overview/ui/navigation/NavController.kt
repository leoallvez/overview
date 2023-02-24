package br.com.deepbyte.overview.ui.navigation

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
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.discover.GenreDiscoverScreen
import br.com.deepbyte.overview.ui.discover.ProviderDiscoverScreen
import br.com.deepbyte.overview.ui.home.HomeScreen
import br.com.deepbyte.overview.ui.media.MediaDetailsScreen
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.navigation.events.HomeScreenEvents
import br.com.deepbyte.overview.ui.navigation.events.MediaDetailsScreenEvents
import br.com.deepbyte.overview.ui.person.CastDetailsScreen
import br.com.deepbyte.overview.ui.search.SearchScreen
import br.com.deepbyte.overview.ui.splash.SplashScreen
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.util.getApiId
import br.com.deepbyte.overview.util.getBackToHome
import br.com.deepbyte.overview.util.getDiscoverParams
import br.com.deepbyte.overview.util.getParams
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
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
            HomeScreen(events = HomeScreenEvents(navController))
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
        arguments = listOf(NavArgument.ID, NavArgument.TYPE, NavArgument.BACK_TO_HOME)
    ) { navBackStackEntry ->
        val events = MediaDetailsScreenEvents(
            navigation = navController,
            backToHome = navBackStackEntry.getBackToHome()
        )
        MediaDetailsScreen(
            params = navBackStackEntry.getParams(),
            events = events
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
        arguments = listOf(NavArgument.JSON)
    ) { navBackStackEntry ->
        ProviderDiscoverScreen(
            events = BasicsMediaEvents(navController),
            params = navBackStackEntry.getDiscoverParams()
        )
    }
    composable(
        route = ScreenNav.GenreDiscover.route,
        arguments = listOf(NavArgument.JSON)
    ) { navBackStackEntry ->
        GenreDiscoverScreen(
            events = BasicsMediaEvents(navController),
            params = navBackStackEntry.getDiscoverParams()
        )
    }
}
