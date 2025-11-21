package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.MediaUiType.MOVIE
import br.dev.singular.overview.presentation.model.MediaUiType.TV

internal fun MediaUiType.toDomain() = when(this) {
    TV -> MediaType.TV
    MOVIE -> MediaType.MOVIE
    else -> MediaType.ALL
}
