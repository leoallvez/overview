package br.dev.singular.overview.data.model

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion

fun MediaDataModel.toDomainModel() = Media(
    id = id,
    type = type.toMediaDomainType(),
    title = betterTitle,
    isLiked = isLiked,
    posterPath = posterPath
)

fun SuggestionDataModel.toDomainModel() = Suggestion(
    path = path,
    order = order,
    type = type.toMediaDomainType(),
    titleKey = titleKey,
    isActive = isActive
)

private fun MediaDataType.toMediaDomainType() = when(this) {
    MediaDataType.MOVIE -> MediaType.MOVIE
    MediaDataType.TV -> MediaType.TV
    MediaDataType.ALL -> MediaType.ALL
    else -> MediaType.UNKNOWN
}
