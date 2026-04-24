package br.dev.singular.overview.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.screens.catalog.details.CatalogDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.catalog.details.CatalogDetailsViewModel
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailActions
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.CatalogSelectionViewModel
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.ChangeCatalogScreen
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.SelectCatalogScreen
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions
import br.dev.singular.overview.presentation.ui.screens.favorites.FavoritesScreen
import br.dev.singular.overview.presentation.ui.screens.favorites.FavoritesViewModel
import br.dev.singular.overview.presentation.ui.screens.favorites.interaction.FavoritesActions
import br.dev.singular.overview.presentation.ui.screens.genre.GenreSelectionViewModel
import br.dev.singular.overview.presentation.ui.screens.genre.SelectGenreScreen
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionActions
import br.dev.singular.overview.presentation.ui.screens.person.PersonDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.person.PersonDetailsViewModel
import br.dev.singular.overview.presentation.ui.screens.person.interaction.PersonDetailsActions
import br.dev.singular.overview.presentation.ui.screens.splash.SplashScreen
import br.dev.singular.overview.presentation.ui.screens.video.YouTubePlayerFullscreen
import br.dev.singular.overview.presentation.ui.screens.video.interaction.YouTubePlayerActions
import br.dev.singular.overview.ui.media.MediaDetailsScreen
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.ui.search.SearchScreen
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.getApiId
import br.dev.singular.overview.util.getParams

@Composable
fun NavController(
    showAds: Boolean,
    modifier: Modifier,
    setEdgeToEdge: (Boolean) -> Unit,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Splash.route,
        modifier = modifier.background(PrimaryBackground)
    ) {
        val nav = NavigationWrapper(navController)
        composable(route = Destination.Splash.route) {
            SplashScreen(
                onToHome = {
                    navController.navigate(route = Destination.SelectCatalog.route) {
                        popUpTo(Destination.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = Destination.SelectCatalog.route,
        ) {

            val viewModel = hiltViewModel<CatalogSelectionViewModel>()

            SelectCatalogScreen(
                uiState = viewModel.uiState.collectAsState().value,
                showTooltip = !viewModel.tooltipDismissed.collectAsState().value,
                actions = CatalogSelectionActions(
                    tagPath = "/select-catalog",
                    navigation = nav,
                    handleIntent = viewModel::handleIntent
                )
            )
        }
        composable(
            route = Destination.ChangeCatalog.route,
            enterTransition = { slideInFromBottom(duration = AnimationDurations.LONG) },
            exitTransition = { slideOutToBottom(duration = AnimationDurations.LONG) },
            popEnterTransition = { fadeIn(animationSpec = tween(AnimationDurations.LONG)) },
            popExitTransition = { slideOutToBottom(duration = AnimationDurations.LONG) }
        ) {

            val viewModel = hiltViewModel<CatalogSelectionViewModel>()

            ChangeCatalogScreen(
                uiState = viewModel.uiState.collectAsState().value,
                actions = CatalogSelectionActions(
                    tagPath = "/change-catalog",
                    navigation = nav,
                    handleIntent = viewModel::handleIntent
                )
            )
        }
        composable(
            route = Destination.SelectGenre.route,
            enterTransition = { slideInFromBottom(duration = AnimationDurations.LONG) },
            exitTransition = { slideOutToBottom(duration = AnimationDurations.LONG) },
            popEnterTransition = { fadeIn(animationSpec = tween(AnimationDurations.LONG)) },
            popExitTransition = { slideOutToBottom(duration = AnimationDurations.LONG) }
        ) {

            val viewModel = hiltViewModel<GenreSelectionViewModel>()

            SelectGenreScreen(
                uiState = viewModel.uiState.collectAsState().value,
                actions = GenreSelectionActions(
                    navigation = nav,
                    handleIntent = viewModel::handleIntent
                )
            )
        }
        composable(route = Destination.CatalogDetails.route) {

            val viewModel = hiltViewModel<CatalogDetailsViewModel>()

            CatalogDetailsScreen(
                queryState = viewModel.queryState.collectAsState().value,
                uiPages = viewModel.medias.collectAsLazyPagingItems(),
                scrollState = viewModel.scrollState.collectAsState().value,
                actions = CatalogDetailActions(
                    navigation = nav,
                    handleIntent = viewModel::handleIntent
                )
            )
        }
        composable(route = Destination.Search.route) {
            SearchScreen(navigate = nav)
        }
        composable(
            route = Destination.MediaDetails.route,
            arguments = listOf(NavArg.ID, NavArg.TYPE, NavArg.BACKSTACK),
            exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
        ) { navBackStackEntry ->
            MediaDetailsScreen(
                params = navBackStackEntry.getParams(),
                navigate = MediaDetailsNavigate(nav = navController)
            )
        }
        composable(
            route = Destination.YouTubePlayer.route,
            arguments = listOf(
                navArgument(name = Destination.VIDEO_KEY_PARAM) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            YouTubePlayerFullscreen(
                setEdgeToEdge = setEdgeToEdge,
                videoKey = backStackEntry.arguments?.getString(Destination.VIDEO_KEY_PARAM) ?: "",
                actions = YouTubePlayerActions(navigation = nav)
            )
        }
        composable(
            route = Destination.PersonDetails.route,
            arguments = listOf(NavArg.ID),
            exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
        ) { navBackStackEntry ->

            val viewModel = hiltViewModel<PersonDetailsViewModel>()

            PersonDetailsScreen(
                personId = navBackStackEntry.getApiId(),
                uiState = viewModel.uiState.collectAsState().value,
                showAds = showAds,
                actions = PersonDetailsActions(
                    navigation = nav,
                    onLoad = viewModel::onLoad
                )
            )
        }
        composable(route = Destination.Favorites.route) {

            val viewModel = hiltViewModel<FavoritesViewModel>()

            FavoritesScreen(
                queryState = viewModel.queryState.collectAsState().value,
                uiPages = viewModel.medias.collectAsLazyPagingItems(),
                scrollState = viewModel.scrollState.collectAsState().value,
                onSetScrollState = { viewModel.onSetScrollState(it) },
                actions = FavoritesActions(
                    navigation = nav,
                    onSetType = viewModel::onSetType
                )
            )
        }
    }
}
