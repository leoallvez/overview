package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class GenreUiState(
    val initial: GenreUiModel?,
    val selected: GenreUiModel?,
    val options: ImmutableList<GenreUiModel>,
    val query: String = ""
) {
    val hasChanged: Boolean = initial?.id != selected?.id

    val isSearching: Boolean = query.isNotBlank()

    val isSearchVisible: Boolean = shouldShowSearch(itemCount = options.size, query = query)

    val filteredOptions: ImmutableList<GenreUiModel> = query.trim().let { text ->
        options.filter { it.name.contains(text, ignoreCase = true) }.toImmutableList()
    }
}
