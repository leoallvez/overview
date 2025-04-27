package br.dev.singular.overview.data.model

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType

fun MediaDataModel.toDomainModel() = Media(
    id = id,
    type = type.toMediaDomainType(),
    title = betterTitle,
    isLiked = isLiked,
    posterPath = posterPath.orEmpty()
)

private fun String?.toMediaDomainType() = when(this) {
    MediaDataType.MOVIE.key -> MediaType.MOVIE
    MediaDataType.TV_SHOW.key -> MediaType.TV_SHOW
    else -> MediaType.ALL
}
