package br.dev.singular.overview.navigation.routes

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.search.SearchScreen
import br.dev.singular.overview.presentation.ui.screens.search.SearchViewModel
import br.dev.singular.overview.presentation.ui.screens.search.interaction.SearchActions

fun NavGraphBuilder.searchRoute(
    navigation: INavigationWrapper
) {
    composable(route = Destination.Search.route) {
        val viewModel = hiltViewModel<SearchViewModel>()

        SearchScreen(
            queryState = viewModel.queryState.collectAsState().value,
            scrollState = viewModel.scrollState.collectAsState().value,
            uiPages = viewModel.medias.collectAsLazyPagingItems(),
            suggestionsUIState = viewModel.suggestionsState.collectAsState().value,
            onSetScrollState = viewModel::onSetScrollState,
            actions = SearchActions(
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
