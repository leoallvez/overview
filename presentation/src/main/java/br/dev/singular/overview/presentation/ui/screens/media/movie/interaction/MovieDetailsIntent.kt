package br.dev.singular.overview.presentation.ui.screens.media.movie.interaction

import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel

sealed class MovieDetailsIntent {

    data class Load(val id: Long) : MovieDetailsIntent()

    data class Like(val media: MediaDetailsUiModel.Movie) : MovieDetailsIntent()

    data class SelectCatalog(val catalog: CatalogUiModel) : MovieDetailsIntent()
}
