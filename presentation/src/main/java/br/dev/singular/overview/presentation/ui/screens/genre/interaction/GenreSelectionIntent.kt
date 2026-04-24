package br.dev.singular.overview.presentation.ui.screens.genre.interaction

import br.dev.singular.overview.presentation.model.GenreUiModel

sealed class GenreSelectionIntent {
    data object Load : GenreSelectionIntent()
    data class Select(val genre: GenreUiModel?) : GenreSelectionIntent()
}
