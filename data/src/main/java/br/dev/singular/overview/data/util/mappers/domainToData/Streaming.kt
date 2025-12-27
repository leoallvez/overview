package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.domain.model.Streaming

internal fun Streaming.toData() = StreamingDataModel(
    id = id,
    name = name,
    priority = priority,
    logoPath = logoPath,
    display = display,
    lastUpdate = lastUpdate
)
