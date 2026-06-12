package br.dev.singular.overview.presentation.ui.screens.catalog.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.ui.components.UiDivider
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogTopAppBar
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.screens.catalog.components.UiFilterType
import br.dev.singular.overview.presentation.ui.screens.catalog.components.UiMainFilter
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailActions
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailIntent
import br.dev.singular.overview.presentation.ui.screens.common.UiPagedMediaGrid
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import br.dev.singular.overview.presentation.ui.utils.fakeQueryState
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection
import br.dev.singular.overview.presentation.ui.utils.rememberLazyGridScrollState

/**
 * Displays the details of a catalog, including a list of its media items.
 *
 * @param queryState The [QueryUiState] that holds the current screen state.
 * @param scrollState The [ScrollUiState] used to restore and save the scroll position.
 * @param uiPages The paginated list of [MediaUiModel] items to be displayed in the grid.
 * @param actions The [CatalogDetailActions] containing the event handlers for the screen.
 */
@Composable
fun CatalogDetailsScreen(
    queryState: QueryUiState,
    scrollState: ScrollUiState,
    uiPages: LazyPagingItems<MediaUiModel>,
    actions: CatalogDetailActions,
) {
    val gridState = rememberLazyGridScrollState(scrollState, actions::onSetScrollState)

    CatalogDetailsContent(
        queryState = queryState,
        actions = actions,
    ) {
        UiPagedMediaGrid(
            tagPath = actions.tagPath,
            items = uiPages,
            gridState = gridState,
            onClickItem = actions::navigateToMediaDetails
        )
    }
}

/**
 * The internal content of the catalog details screen.
 *
 * @param queryState The [QueryUiState] that holds the current screen state.
 * @param actions The [CatalogDetailActions] containing the event handlers.
 * @param content The composable content representing the media items grid.
 */
@Composable
private fun CatalogDetailsContent(
    queryState: QueryUiState,
    actions: CatalogDetailActions,
    content: @Composable () -> Unit
) {
    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    UiScaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            UiCatalogTopAppBar(catalog = queryState.catalog, isCollapsed = isCollapsed)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .animateContentSize()
        ) {
            UiMainFilter(
                query = queryState,
                visible = !isCollapsed && queryState.catalog != null,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_4x)),
                onClickFilter = {
                    when (it) {
                        is UiFilterType.Type -> actions.onSetType(it.value)
                        is UiFilterType.Genre -> actions.navigateToGenre()
                        is UiFilterType.Catalog -> actions.navigateToCatalog()
                    }
                }
            )
            UiDivider(visible = isCollapsed)
            content()
        }
    }
}

@UiScreenPreview
@Composable
private fun CatalogDetailsContentPreview() {

    var queryState by remember { mutableStateOf(fakeQueryState()) }

    CatalogDetailsContent(
        queryState = queryState,
        actions = CatalogDetailActions(
            handleIntent = { intent ->
                if (intent is CatalogDetailIntent.SelectType) {
                    queryState = queryState.copy(type = intent.type)
                }
            }
        ),
    ) {
        UiMediaGrid(items = fakeMedias(90))
    }
}
