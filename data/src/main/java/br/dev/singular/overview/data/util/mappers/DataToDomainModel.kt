package br.dev.singular.overview.data.util.mappers

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.SuggestionDataModel
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
    key = sourceKey,
    order = order,
    type = type.toMediaDomainType(),
    isActive = isActive
)

internal fun MediaDataType.toMediaDomainType() = when(this) {
    MediaDataType.MOVIE -> MediaType.MOVIE
    MediaDataType.TV -> MediaType.TV
    MediaDataType.ALL -> MediaType.ALL
    else -> MediaType.UNKNOWN
}