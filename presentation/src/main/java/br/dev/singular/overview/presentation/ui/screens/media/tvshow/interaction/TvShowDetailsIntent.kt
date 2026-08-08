package br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction

import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel

sealed class TvShowDetailsIntent {

    data class Load(val id: Long) : TvShowDetailsIntent()

    data class Like(val media: MediaDetailsUiModel.TvShow) : TvShowDetailsIntent()

    data class SelectCatalog(val catalog: CatalogUiModel) : TvShowDetailsIntent()
}
