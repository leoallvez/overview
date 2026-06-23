package br.dev.singular.overview.presentation.ui.screens.search.interaction

import br.dev.singular.overview.presentation.model.MediaUiType

sealed class SearchIntent {
    data object LoadSuggestions : SearchIntent()
    data class Search(val query: String) : SearchIntent()
    data class SetType(val type: MediaUiType) : SearchIntent()
}
