package br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.presentation.model.StreamingUiModel
import java.util.Date

internal fun StreamingUiModel.toDomain() = Streaming(
    id = id,
    name = name,
    priority = priority,
    display = true,
    // TODO: create a logic to get the logo path
    logoPath = logoURL,
    lastUpdate = Date()
)
