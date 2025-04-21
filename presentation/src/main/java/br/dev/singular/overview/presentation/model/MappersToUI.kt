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
    //TODO: get full URL
    posterURLPath = ""//"${BuildConfig.IMG_URL}$posterPath"
)

internal fun MediaType.toMediaUIType() = when(this) {
    MediaType.MOVIE -> MediaUIType.MOVIE
    else -> MediaUIType.TV_SHOW
}
