package br.dev.singular.overview.presentation.model

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.presentation.ui.utils.toMediaUIType

fun List<Suggestion>.toUIMap() = associate { it.titleKey to it.medias.map(Media::toUIModel) }

fun Media.toUIModel() = MediaUIModel(
    id = id,
    type = type.toMediaUIType(),
    title = title,
    isLiked = isLiked,
    posterURL = buildImageFullURL(posterPath)
)

private fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path}"
