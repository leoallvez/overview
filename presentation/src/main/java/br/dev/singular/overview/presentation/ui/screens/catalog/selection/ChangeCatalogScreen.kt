package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeCatalogs

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
internal fun ChangeCatalogsScreenSuccessPreview() {
    val selectedId = remember { mutableLongStateOf(value = 0L) }
    val uiState = UiState.Success(
        CatalogUiState(
            selectedId = selectedId.longValue,
            options = fakeCatalogs(30)
        )
    )
    ChangeCatalogScreen(
        uiState = uiState,
        actions = CatalogSelectionActions(
            handleIntent = { intent ->
                if (intent is CatalogSelectionIntent.Select) {
                    selectedId.longValue = intent.catalog.id
                }
            }
        )
    )
}

@UiScreenPreview
@Composable
internal fun ChangeCatalogScreenLoadingPreview() {
    ChangeCatalogScreen(
        uiState = UiState.Loading(),
        actions = CatalogSelectionActions()
    )
}

@UiScreenPreview
@Composable
internal fun ChangeCatalogScreenErrorPreview() {
    ChangeCatalogScreen(
        uiState = UiState.Error(),
        actions = CatalogSelectionActions()
    )
}
