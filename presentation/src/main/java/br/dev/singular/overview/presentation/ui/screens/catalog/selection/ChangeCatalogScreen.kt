package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.UiTopAppBar
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeCatalog

/**
 * A screen that allows the user to select a catalog.
 * @param uiState The state of the UI, which can be loading, success, or error.
 * @param actions The actions to be performed on the screen.
 */
@Composable
fun ChangeCatalogScreen(
    uiState: UiState<CatalogUiState>,
    actions: CatalogSelectionActions
) {
    CatalogContent(
        tagPath = actions.tagPath,
        selectionEnabled = true,
        uiState = uiState,
        onLoad = { actions.onLoad() },
        onSelected = { actions.onSelect(it, clearGenre = false) },
        toolbar = {
            UiTopAppBar(
                title = stringResource(R.string.change_catalog),
                onBack = actions::onBack
            )
        }
    )
}

@UiScreenPreview
@Composable
private fun ChangeCatalogScreenSuccessPreview() {
    var selectedId by remember { mutableLongStateOf(value = 0L) }
    val uiState = UiState.Success(
        CatalogUiState(
            selectedId = selectedId,
            options = fakeCatalog(30)
        )
    )
    ChangeCatalogScreen(
        uiState = uiState,
        actions = CatalogSelectionActions(
            handleIntent = { intent ->
                if (intent is CatalogSelectionIntent.Select) selectedId = intent.catalog.id
            }
        )
    )
}

@UiScreenPreview
@Composable
private fun ChangeCatalogScreenLoadingPreview() {
    ChangeCatalogScreen(
        uiState = UiState.Loading(),
        actions = CatalogSelectionActions()
    )
}

@UiScreenPreview
@Composable
private fun ChangeCatalogScreenErrorPreview() {
    ChangeCatalogScreen(
        uiState = UiState.Error(),
        actions = CatalogSelectionActions()
    )
}
