package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.UiInfoTooltip
import br.dev.singular.overview.presentation.ui.components.UiTopAppBar
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeCatalog

/**
 * Composable that displays a screen to choose a catalog.
 *
 * @param uiState The state of the UI, which can be loading, success, or error.
 * @param showTooltip Whether to show the info tooltip.
 * @param actions The actions to be performed on the screen.
 */
@Composable
fun SelectCatalogScreen(
    uiState: UiState<CatalogUiState>,
    showTooltip: Boolean = false,
    actions: CatalogSelectionActions = CatalogSelectionActions()
) {
    CatalogContent(
        tagPath = actions.tagPath,
        selectionEnabled = false,
        uiState = uiState,
        onLoad = { actions.onLoad() },
        onSelected = { actions.onSelect(it, clearGenre = true) },
        toolbar = { UiTopAppBar(title = stringResource(R.string.select_catalog)) },
        tooltip = {
            UiInfoTooltip(
                visible = showTooltip,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_3x)),
                message = stringResource(R.string.catalog_only_message),
                onClose = { actions.onDismissTooltip() }
            )
        }
    )
}

@UiScreenPreview
@Composable
private fun SelectCatalogScreenSuccessPreview() {
    var showTooltip by remember { mutableStateOf(true) }
    SelectCatalogScreen(
        showTooltip = showTooltip,
        uiState = UiState.Success(
            data = CatalogUiState(
                selectedId = 0,
                options = fakeCatalog(30)
            )
        ),
        actions = CatalogSelectionActions(
            handleIntent = { intent ->
                if (intent is CatalogSelectionIntent.DismissTooltip) showTooltip = false
            }
        )
    )
}

@UiScreenPreview
@Composable
private fun SelectCatalogScreenLoadingPreview() {
    SelectCatalogScreen(
        uiState = UiState.Loading()
    )
}

@UiScreenPreview
@Composable
private fun SelectCatalogScreenErrorPreview() {
    SelectCatalogScreen(
        uiState = UiState.Error()
    )
}
