package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CatalogUiState(
    val initial: CatalogUiModel?,
    val selected: CatalogUiModel?,
    val options: ImmutableList<CatalogUiModel>,
    val query: String = ""
) {
    val hasChanged: Boolean = initial?.id != selected?.id

    val isSearchVisible: Boolean = shouldShowSearch(itemCount = options.size, query = query)

    val filteredOptions: ImmutableList<CatalogUiModel> = query.trim().let { text ->
        options.filter { it.name.contains(text, ignoreCase = true) }.toImmutableList()
    }
}
