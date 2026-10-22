package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.VideoDataModel
import br.dev.singular.overview.domain.model.Video

internal fun VideoDataModel.toDomain() = Video(
    id = id,
    key = key,
    name = name,
)

internal fun List<VideoDataModel>.toDomain() = map { it.toDomain() }
