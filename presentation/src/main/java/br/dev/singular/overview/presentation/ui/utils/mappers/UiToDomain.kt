package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUiParam
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.MediaUiType.*

fun MediaUiType.toDomain() = when(this) {
    TV -> MediaType.TV
    MOVIE -> MediaType.MOVIE
    else -> MediaType.ALL
}

fun MediaUiParam.toDomain() = MediaParam(
    key = key,
    type = type.toDomain(),
    isLiked = isLiked,
    query = query,
    page = page
)
