package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList

data class GenreUiState(
    val selectedId: Long?,
    val options: ImmutableList<GenreUiModel>
)
