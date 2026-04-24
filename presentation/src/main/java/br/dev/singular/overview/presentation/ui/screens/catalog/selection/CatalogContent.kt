package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.UiList
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogItem
import br.dev.singular.overview.presentation.ui.screens.common.ItemListSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import kotlinx.collections.immutable.toImmutableList

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
    onSelected: (CatalogUiModel) -> Unit
) {

    LaunchedEffect(Unit) { onLoad() }
    UiScaffold(
        topBar = toolbar,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
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
                UiList(
                    items = data.options.toImmutableList()
                ) {
                    UiCatalogItem(
                        model = it,
                        selected = if (selectionEnabled) it.id == data.selectedId else null
                    ) {
                        onSelected(it)
                    }
                }
            }
        }
    }
}
