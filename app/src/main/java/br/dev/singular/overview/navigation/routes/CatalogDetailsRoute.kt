package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.catalog.details.CatalogDetailsScreen
import br.dev.singular.overview.presentation.ui.screens.catalog.details.CatalogDetailsViewModel
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailActions

fun NavGraphBuilder.catalogDetailsRoute(
    navigation: INavigationWrapper
) {
    composable(route = Destination.CatalogDetails.route) {
        val viewModel = hiltViewModel<CatalogDetailsViewModel>()

        CatalogDetailsScreen(
            queryState = viewModel.queryState.collectAsState().value,
            uiPages = viewModel.medias.collectAsLazyPagingItems(),
            scrollState = viewModel.scrollState.collectAsState().value,
            actions = CatalogDetailActions(
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
