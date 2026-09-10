package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList

data class CatalogUiState(
    val initial: CatalogUiModel?,
    val selected: CatalogUiModel?,
    val options: ImmutableList<CatalogUiModel>
) {
    val hasChanged: Boolean = initial?.id != selected?.id
}
