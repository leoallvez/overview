package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.buildPosterURL
import kotlinx.collections.immutable.toImmutableList

internal fun Media.toUi() = MediaUiModel(
    id = id,
    type = type.toUi(),
    title = title,
    isLiked = isLiked,
    previewDrawableRes = null,
    posterURL = buildPosterURL(posterPath)
)

internal fun List<Media>.toUi() = map { it.toUi() }.toImmutableList()
