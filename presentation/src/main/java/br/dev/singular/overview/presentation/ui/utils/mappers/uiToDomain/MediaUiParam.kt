package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.presentation.model.MediaUiParam

internal fun MediaUiParam.toDomain() = MediaParam(
    key = key,
    type = type.toDomain(),
    isLiked = isLiked,
    query = query,
    page = page
)
