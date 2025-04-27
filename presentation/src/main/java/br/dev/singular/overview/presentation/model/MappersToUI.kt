package br.dev.singular.overview.presentation.model

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.presentation.BuildConfig

fun List<Suggestion>.toUIMap() = associate { it.titleKey to it.medias.map(Media::toUIModel) }

fun Media.toUIModel() = MediaUIModel(
    id = id,
    type = type.toMediaUIType(),
    title = title,
    isLiked = isLiked,
    posterURL = buildImageFullURL(posterPath)
)

private fun MediaType.toMediaUIType() = when(this) {
    MediaType.MOVIE -> MediaUIType.MOVIE
    MediaType.TV_SHOW -> MediaUIType.TV_SHOW
    else -> MediaUIType.ALL
}

private fun buildImageFullURL(path: String) = "${BuildConfig.IMG_URL}$path}"
