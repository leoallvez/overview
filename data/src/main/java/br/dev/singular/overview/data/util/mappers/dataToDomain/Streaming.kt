package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.domain.model.Streaming

internal fun StreamingDataModel.toDomain() = Streaming(
    id = id,
    name = name,
    priority = priority,
    logoPath = logoPath,
    display = display,
    lastUpdate = lastUpdate
)
