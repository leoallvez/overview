package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.tooltip.UiTooltip
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionActions
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeCatalogs

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
        onSearch = actions::onSearch,
        onClear = actions::onClear,
        onSelected = { actions.onSelect(it, clearGenre = true) },
        toolbar = { UiTopAppBar(title = stringResource(R.string.select_catalog)) },
        tooltip = {
            UiTooltip(
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
internal fun SelectCatalogsScreenSuccessPreview() {
    val showTooltip = remember { mutableStateOf(true) }
    val catalogs = fakeCatalogs(30)
    SelectCatalogScreen(
        showTooltip = showTooltip.value,
        uiState = UiState.Success(
            data = CatalogUiState(
                initial = catalogs.first(),
                selected = catalogs.first(),
                options = catalogs
            )
        ),
        actions = CatalogSelectionActions(
            handleIntent = { intent ->
                if (intent is CatalogSelectionIntent.DismissTooltip) {
                    showTooltip.value = false
                }
            }
        )
    )
}

@UiScreenPreview
@Composable
internal fun SelectCatalogScreenSearchPreview() {
    val query = remember { mutableStateOf("Lorem") }
    val catalogs = fakeCatalogs(30)
    SelectCatalogScreen(
        uiState = UiState.Success(
            data = CatalogUiState(
                initial = catalogs.first(),
                selected = catalogs.first(),
                options = catalogs,
                query = query.value
            )
        ),
        actions = CatalogSelectionActions(
            handleIntent = { intent ->
                if (intent is CatalogSelectionIntent.Search) {
                    query.value = intent.query
                }
            }
        )
    )
}

@UiScreenPreview
@Composable
internal fun SelectCatalogScreenLoadingPreview() {
    SelectCatalogScreen(
        uiState = UiState.Loading()
    )
}

@UiScreenPreview
@Composable
internal fun SelectCatalogScreenErrorPreview() {
    SelectCatalogScreen(
        uiState = UiState.Error()
    )
}
