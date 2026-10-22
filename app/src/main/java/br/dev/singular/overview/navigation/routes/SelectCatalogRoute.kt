package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.CatalogSelectionViewModel
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.SelectCatalogScreen
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions

fun NavGraphBuilder.selectCatalogRoute(
    navigation: INavigationWrapper
) {
    composable(route = Destination.SelectCatalog.route) {
        val viewModel = hiltViewModel<CatalogSelectionViewModel>()

        SelectCatalogScreen(
            uiState = viewModel.uiState.collectAsState().value,
            showTooltip = !viewModel.tooltipDismissed.collectAsState().value,
            actions = CatalogSelectionActions(
                tagPath = "/select-catalog",
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
