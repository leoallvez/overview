package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page

internal fun MediaDataModel.toDomain() = Media(
    id = id,
    type = type.toDomain(),
    title = betterTitle,
    isLiked = isLiked,
    posterPath = posterPath,
    lastUpdate = lastUpdate
)

internal fun MediaDataPage.toDomain(): Page<Media> = Page(
    items = items.map { it.toDomain() },
    currentPage = page,
    isLastPage = isLastPage
)
