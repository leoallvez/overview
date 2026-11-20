package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.navigation.NavArg
import br.dev.singular.overview.presentation.ui.navigation.AnimationDurations
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.navigation.getApiId
import br.dev.singular.overview.presentation.ui.navigation.rightExitTransition
import br.dev.singular.overview.presentation.ui.screens.person.PersonDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.person.PersonDetailsViewModel
import br.dev.singular.overview.presentation.ui.screens.person.interaction.PersonDetailsActions

fun NavGraphBuilder.personDetailsRoute(
    navigation: INavigationWrapper,
    showAds: Boolean
) {
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
                navigation = navigation,
                onLoad = viewModel::onLoad
            )
        )
    }
}
