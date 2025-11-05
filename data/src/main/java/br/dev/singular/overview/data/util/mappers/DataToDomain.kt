package br.dev.singular.overview.data.util.mappers

import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaDataType.*
import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.model.Suggestion

fun MediaDataModel.toDomain() = Media(
    id = id,
    type = type.toDomain(),
    title = betterTitle,
    isLiked = isLiked,
    posterPath = posterPath,
    lastUpdate = lastUpdate
)

fun SuggestionDataModel.toDomain() = Suggestion(
    id = id,
    key = sourceKey,
    order = order,
    type = type.toDomain(),
    isActive = isActive,
    lastUpdate = lastUpdate
)

fun MediaDataPage.toDomain(): Page<Media> = Page(
    items = items.map { it.toDomain() },
    currentPage = page,
    isLastPage = isLastPage
)

internal fun MediaDataType.toDomain() = when(this) {
    MOVIE -> MediaType.MOVIE
    TV -> MediaType.TV
    ALL -> MediaType.ALL
    else -> MediaType.UNKNOWN
}