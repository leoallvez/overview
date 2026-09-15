package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.presentation.ui.navigation.AnimationDurations
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.navigation.rightExitTransition
import br.dev.singular.overview.presentation.ui.screens.user.UserViewModel
import br.dev.singular.overview.presentation.ui.screens.user.profile.ProfileScreen
import br.dev.singular.overview.presentation.ui.screens.user.profile.interaction.ProfileActions

fun NavGraphBuilder.profileRoute(
    navigation: INavigationWrapper
) {
    composable(
        route = Destination.Profile.route,
        exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
    ) {
        val viewModel = hiltViewModel<UserViewModel>()

        ProfileScreen(
            uiState = viewModel.uiState.collectAsState().value,
            actionState = viewModel.actionState.collectAsState().value,
            actions = ProfileActions(
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
