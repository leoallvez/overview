package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.navigation.fadeInTransition
import br.dev.singular.overview.presentation.ui.navigation.slideInFromBottom
import br.dev.singular.overview.presentation.ui.navigation.slideOutToBottom
import br.dev.singular.overview.presentation.ui.screens.genre.GenreSelectionViewModel
import br.dev.singular.overview.presentation.ui.screens.genre.SelectGenreScreen
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionActions

fun NavGraphBuilder.selectGenreRoute(
    navigation: INavigationWrapper
) {
    composable(
        route = Destination.SelectGenre.route,
        enterTransition = { slideInFromBottom() },
        exitTransition = { slideOutToBottom() },
        popEnterTransition = { fadeInTransition() },
        popExitTransition = { slideOutToBottom() }
    ) {
        val viewModel = hiltViewModel<GenreSelectionViewModel>()

        SelectGenreScreen(
            uiState = viewModel.uiState.collectAsState().value,
            actions = GenreSelectionActions(
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
