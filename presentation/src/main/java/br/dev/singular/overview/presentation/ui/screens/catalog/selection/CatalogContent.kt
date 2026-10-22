package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.UiList
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiSearchField
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogItem
import br.dev.singular.overview.presentation.ui.screens.common.ItemListSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.StateScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult

/**
 * A composable that displays a catalog of catalog.
 * It handles different UI states (loading, success, error) and allows for item selection.
 *
 * @param tagPath The path for analytics tagging.
 * @param selectionEnabled If true, radio buttons are shown for single selection. If false, navigation chevrons are shown.
 * @param uiState The state of the UI, containing the list of catalog and the selected catalog ID.
 * @param onLoad Callback to be invoked to load the catalog data.
 * @param tooltip An optional composable to be displayed as a tooltip.
 * @param toolbar A composable to be displayed as the top toolbar.
 * @param bottomBar A composable to be displayed as the bottom bar.
 * @param onSearch Callback to be invoked when the search query changes.
 * @param onClear Callback to be invoked when the search field is cleared.
 * @param onSelected Callback to be invoked when a catalog is selected.
 */
@Composable
internal fun CatalogContent(
    tagPath: String,
    selectionEnabled: Boolean,
    uiState: UiState<CatalogUiState>,
    onLoad: () -> Unit,
    tooltip: @Composable () -> Unit = {},
    toolbar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    onSearch: (String) -> Unit = {},
    onClear: () -> Unit = {},
    onSelected: (CatalogUiModel) -> Unit
) {

    LaunchedEffect(Unit) { onLoad() }
    UiScaffold(
        topBar = toolbar,
        bottomBar = bottomBar,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            tooltip()
            UiStateResult(
                uiState = uiState,
                tagPath = tagPath,
                onRefresh = onLoad,
                loadingContent = {
                    ItemListSkeletonScreen(tagPath)
                },
            ) { data ->
                CatalogList(
                    tagPath = tagPath,
                    selectionEnabled = selectionEnabled,
                    data = data,
                    onSearch = onSearch,
                    onClear = onClear,
                    onSelected = onSelected
                )
            }
        }
    }
}

/**
 * The list of catalogs, with the search field displayed when there are many catalogs.
 *
 * @param tagPath The path for analytics tagging.
 * @param selectionEnabled If true, radio buttons are shown for single selection.
 * @param data The [CatalogUiState] with the catalogs, the selected catalog and the search query.
 * @param onSearch Callback to be invoked when the search query changes.
 * @param onClear Callback to be invoked when the search field is cleared.
 * @param onSelected Callback to be invoked when a catalog is selected.
 */
@Composable
private fun CatalogList(
    tagPath: String,
    selectionEnabled: Boolean,
    data: CatalogUiState,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onSelected: (CatalogUiModel) -> Unit
) {
    Column {
        UiAnimatedVisibility(visible = data.isSearchVisible) {
            UiSearchField(
                query = data.query,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_2x)),
                placeholder = stringResource(R.string.search_in_catalogs),
                onQueryChange = onSearch,
                onClear = onClear
            )
        }
        if (data.filteredOptions.isEmpty()) {
            StateScreen(
                title = stringResource(R.string.not_found),
                tagPath = tagPath,
                tagStatus = "nothing_found"
            )
        } else {
            UiList(items = data.filteredOptions) {
                UiCatalogItem(
                    model = it,
                    selected = if (selectionEnabled) it.id == data.selected?.id else null
                ) {
                    onSelected(it)
                }
            }
        }
    }
}
