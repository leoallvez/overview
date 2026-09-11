package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.button.UiConfirmButton
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
        onSelected = {
            actions.onUpdate(it)
        },
        toolbar = {
            UiTopAppBar(
                title = stringResource(R.string.change_catalog),
                onBack = actions::onBack
            )
        },
        bottomBar = {
            if (uiState !is UiState.Success) return@CatalogContent
            UiConfirmButton(
                enabled = uiState.data.hasChanged,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.spacing_2x),
                    bottom = dimensionResource(R.dimen.spacing_1x)
                ),
                onClick = {
                    uiState.data.selected?.let {
                        actions.onSelect(catalog = it, clearGenre = false)
                    }
                }
            )
        }
    )
}

@UiScreenPreview
@Composable
internal fun ChangeCatalogsScreenSuccessPreview() {
    val selectedId = remember { mutableLongStateOf(value = 0L) }
    val catalogs = fakeCatalogs(30)
    val uiState = UiState.Success(
        CatalogUiState(
            initial = catalogs.first(),
            selected = catalogs.find { it.id == selectedId.longValue },
            options = catalogs
        )
    )
    ChangeCatalogScreen(
        uiState = uiState,
        actions = CatalogSelectionActions(
            handleIntent = { intent ->
                when (intent) {
                    is CatalogSelectionIntent.Select -> selectedId.longValue = intent.catalog.id
                    is CatalogSelectionIntent.Update -> selectedId.longValue = intent.catalog.id
                    else -> {}
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
