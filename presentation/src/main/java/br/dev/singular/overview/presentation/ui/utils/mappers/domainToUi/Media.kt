package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.buildImageFullURL

internal fun Media.toUi() = MediaUiModel(
    id = id,
    type = type.toUi(),
    title = title,
    isLiked = isLiked,
    posterURL = buildImageFullURL(posterPath)
)
