package br.dev.singular.overview.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.navigation.NavArg
import br.dev.singular.overview.presentation.ui.navigation.AnimationDurations
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.navigation.getApiId
import br.dev.singular.overview.presentation.ui.navigation.rightExitTransition
import br.dev.singular.overview.presentation.ui.screens.episodes.EpisodesScreen
import br.dev.singular.overview.presentation.ui.screens.episodes.interaction.EpisodesActions

fun NavGraphBuilder.episodesRoute(
    navigation: INavigationWrapper
) {
    composable(
        route = Destination.Episodes.route,
        arguments = listOf(NavArg.ID),
        exitTransition = { rightExitTransition(duration = AnimationDurations.SMALL) }
    ) { navBackStackEntry ->
        EpisodesScreen(
            tvShowId = navBackStackEntry.getApiId(),
            actions = EpisodesActions(navigation = navigation)
        )
    }
}
