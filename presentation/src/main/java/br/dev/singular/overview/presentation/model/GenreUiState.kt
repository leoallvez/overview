package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList

data class GenreUiState(
    val initial: GenreUiModel?,
    val selected: GenreUiModel?,
    val options: ImmutableList<GenreUiModel>
) {
    val hasChanged: Boolean = initial?.id != selected?.id
}
