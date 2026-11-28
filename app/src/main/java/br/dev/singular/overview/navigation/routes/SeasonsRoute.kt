package br.dev.singular.overview.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.navigation.NavArg
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.ui.navigation.AnimationDurations
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.navigation.getApiId
import br.dev.singular.overview.presentation.ui.navigation.rightExitTransition
import br.dev.singular.overview.presentation.ui.screens.seasons.SeasonsScreen
import br.dev.singular.overview.presentation.ui.screens.seasons.interaction.SeasonsActions
import br.dev.singular.overview.presentation.ui.utils.fakeSeasonsScreenUiModel

fun NavGraphBuilder.seasonsRoute(
    navigation: INavigationWrapper
) {
    composable(
        route = Destination.Seasons.route,
        arguments = listOf(NavArg.ID),
        exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
    ) { navBackStackEntry ->
        SeasonsScreen(
            tvShowId = navBackStackEntry.getApiId(),
            uiState = UiState.Success(fakeSeasonsScreenUiModel()),
            actions = SeasonsActions(navigation = navigation)
        )
    }
}
