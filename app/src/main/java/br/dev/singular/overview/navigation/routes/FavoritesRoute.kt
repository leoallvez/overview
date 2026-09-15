package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.auth.rememberGoogleIdTokenRequest
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.UserUiModel
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHostState
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.favorites.FavoritesScreen
import br.dev.singular.overview.presentation.ui.screens.favorites.FavoritesViewModel
import br.dev.singular.overview.presentation.ui.screens.favorites.interaction.FavoritesActions
import br.dev.singular.overview.presentation.ui.screens.user.UserViewModel
import br.dev.singular.overview.presentation.ui.screens.user.login.LoginScreen
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginActions

fun NavGraphBuilder.favoritesRoute(
    navigation: INavigationWrapper,
    snackbarHostState: UiSnackbarHostState
) {
    composable(route = Destination.Favorites.route) {
        val userViewModel = hiltViewModel<UserViewModel>()

        val user = when (val userState = userViewModel.uiState.collectAsState().value) {
            is UiState.Loading -> return@composable
            is UiState.Success -> userState.data
            is UiState.Error -> null
        }

        if (user == null) {
            Login(userViewModel, snackbarHostState)
        } else {
            Favorites(user = user, navigation = navigation)
        }
    }
}

@Composable
private fun Favorites(
    user: UserUiModel,
    navigation: INavigationWrapper
) {
    val viewModel = hiltViewModel<FavoritesViewModel>()

    FavoritesScreen(
        user = user,
        uiPages = viewModel.medias.collectAsLazyPagingItems(),
        queryState = viewModel.queryState.collectAsState().value,
        scrollState = viewModel.scrollState.collectAsState().value,
        onSetScrollState = { viewModel.onSetScrollState(it) },
        actions = FavoritesActions(
            navigation = navigation,
            onSetType = viewModel::onSetType
        )
    )
}

@Composable
private fun Login(
    viewModel: UserViewModel,
    snackbarHostState: UiSnackbarHostState
) {
    LoginScreen(
        actionState = viewModel.actionState.collectAsState().value,
        actions = LoginActions(
            handleIntent = viewModel::handleIntent,
            onRequestIdToken = rememberGoogleIdTokenRequest(
                onResult = viewModel::handleIntent
            ),
            onShowSnackbar = snackbarHostState::show
        )
    )
}
