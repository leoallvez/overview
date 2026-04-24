package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList

data class CatalogUiState(
    val selectedId: Long?,
    val options: ImmutableList<CatalogUiModel>
)
