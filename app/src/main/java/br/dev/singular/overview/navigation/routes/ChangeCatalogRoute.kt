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
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.CatalogSelectionViewModel
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.ChangeCatalogScreen
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions

fun NavGraphBuilder.changeCatalogRoute(
    navigation: INavigationWrapper
) {
    composable(
        route = Destination.ChangeCatalog.route,
        enterTransition = { slideInFromBottom() },
        exitTransition = { slideOutToBottom() },
        popEnterTransition = { fadeInTransition() },
        popExitTransition = { slideOutToBottom() }
    ) {
        val viewModel = hiltViewModel<CatalogSelectionViewModel>()

        ChangeCatalogScreen(
            uiState = viewModel.uiState.collectAsState().value,
            actions = CatalogSelectionActions(
                tagPath = "/change-catalog",
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
