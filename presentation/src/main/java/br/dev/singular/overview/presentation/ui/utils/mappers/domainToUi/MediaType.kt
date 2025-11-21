package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUiType

internal fun MediaType.toUi() = when(this) {
    MediaType.MOVIE -> MediaUiType.MOVIE
    MediaType.TV -> MediaUiType.TV
    else -> MediaUiType.ALL
}
