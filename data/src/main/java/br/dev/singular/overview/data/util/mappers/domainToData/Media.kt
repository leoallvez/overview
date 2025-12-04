package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.domain.model.Media

internal fun Media.toData() = MediaDataModel(
    id = id,
    type = type.toData(),
    title = title,
    name = title,
    isLiked = isLiked,
    posterPath = posterPath,
    lastUpdate = lastUpdate
)
