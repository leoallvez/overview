package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.navigation.FeatureFlags
import br.dev.singular.overview.navigation.NavArg
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHostState
import br.dev.singular.overview.presentation.ui.navigation.AnimationDurations
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.navigation.getParams
import br.dev.singular.overview.presentation.ui.navigation.rightExitTransition
import br.dev.singular.overview.presentation.ui.screens.media.movie.MovieDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.media.movie.MovieDetailsViewModel
import br.dev.singular.overview.presentation.ui.screens.media.movie.interaction.rememberMovieDetailsActions
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.TvShowDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.TvShowDetailsViewModel
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction.rememberTvShowDetailsActions

fun NavGraphBuilder.mediaDetailsRoute(
    navigation: INavigationWrapper,
    flags: FeatureFlags,
    snackbarHostState: UiSnackbarHostState
) {
    composable(
        route = Destination.MediaDetails.route,
        arguments = listOf(NavArg.ID, NavArg.TYPE, NavArg.BACKSTACK),
        exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
    ) { navBackStackEntry ->
        val (id: Long, typeKey: String) = navBackStackEntry.getParams()

        when (MediaUiType.getByName(typeKey)) {
            MediaUiType.MOVIE -> MovieDetails(id, flags, navigation, snackbarHostState)
            else -> TvShowDetails(id, flags, navigation, snackbarHostState)
        }
    }
}

@Composable
private fun MovieDetails(
    movieId: Long,
    flags: FeatureFlags,
    navigation: INavigationWrapper,
    snackbarHostState: UiSnackbarHostState
) {
    val viewModel = hiltViewModel<MovieDetailsViewModel>()

    MovieDetailsScreen(
        movieId = movieId,
        showAds = flags.showAds,
        showAiPrediction = flags.showAiPrediction,
        loginRequired = viewModel.loginRequired.collectAsState().value,
        favoriteAdded = viewModel.favoriteAdded.collectAsState().value,
        uiState = viewModel.uiState.collectAsState().value,
        actions = rememberMovieDetailsActions(
            navigation = navigation,
            handleIntent = viewModel::handleIntent,
            onShowSnackbar = snackbarHostState::show
        )
    )
}

@Composable
private fun TvShowDetails(
    tvShowId: Long,
    flags: FeatureFlags,
    navigation: INavigationWrapper,
    snackbarHostState: UiSnackbarHostState
) {
    val viewModel = hiltViewModel<TvShowDetailsViewModel>()

    TvShowDetailsScreen(
        tvShowId = tvShowId,
        showAds = flags.showAds,
        showAiPrediction = flags.showAiPrediction,
        loginRequired = viewModel.loginRequired.collectAsState().value,
        favoriteAdded = viewModel.favoriteAdded.collectAsState().value,
        uiState = viewModel.uiState.collectAsState().value,
        actions = rememberTvShowDetailsActions(
            navigation = navigation,
            handleIntent = viewModel::handleIntent,
            onShowSnackbar = snackbarHostState::show
        )
    )
}
