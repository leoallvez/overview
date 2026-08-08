package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Video
import br.dev.singular.overview.presentation.model.VideoUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.buildThumbnailUrl
import kotlinx.collections.immutable.toImmutableList

internal fun Video.toUi() = VideoUiModel(
    id = id,
    name = name,
    key = key,
    thumbnailURL = buildThumbnailUrl(key),
    previewDrawableRes = null
)

internal fun List<Video>.toUi() = map { it.toUi() }.toImmutableList()
