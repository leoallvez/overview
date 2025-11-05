package br.dev.singular.overview.data.util.mappers

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaType.*
import br.dev.singular.overview.domain.model.Suggestion

fun Media.toData() = MediaDataModel(
    id = id,
    type = type.toData(),
    title = title,
    name = title,
    isLiked = isLiked,
    posterPath = posterPath,
    lastUpdate = lastUpdate
)

fun Suggestion.toData() = SuggestionDataModel(
    id = id,
    order = order,
    type = type.toData(),
    sourceKey = key,
    isActive = isActive,
    lastUpdate = lastUpdate
)

internal fun MediaType.toData() = when(this) {
    MOVIE -> MediaDataType.MOVIE
    TV -> MediaDataType.TV
    ALL -> MediaDataType.ALL
    else -> MediaDataType.UNKNOWN
}
