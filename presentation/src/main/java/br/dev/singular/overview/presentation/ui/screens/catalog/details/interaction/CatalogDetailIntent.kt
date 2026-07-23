package br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction

import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.ScrollUiState

sealed class CatalogDetailIntent {
    data class SelectType(val type: MediaUiType) : CatalogDetailIntent()
    data class SelectGenre(val genre: GenreUiModel) : CatalogDetailIntent()
    data class SelectCatalog(val catalog: CatalogUiModel) : CatalogDetailIntent()
    data class ChangeScrollState(val scrollState: ScrollUiState) : CatalogDetailIntent()
}
