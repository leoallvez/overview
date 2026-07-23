package br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction

import br.dev.singular.overview.presentation.model.CatalogUiModel

sealed class CatalogSelectionIntent {
    data object Load : CatalogSelectionIntent()
    data class Select(
        val catalog: CatalogUiModel,
        val clearGenre: Boolean = false
    ) : CatalogSelectionIntent()

    data object DismissTooltip : CatalogSelectionIntent()
}
