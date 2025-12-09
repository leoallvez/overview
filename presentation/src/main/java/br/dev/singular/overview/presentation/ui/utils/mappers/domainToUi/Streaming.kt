package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.buildImageFullURL
import kotlinx.collections.immutable.toImmutableList

internal fun List<Streaming>.toUi() = map(Streaming::toUi).toImmutableList()

internal fun Streaming.toUi() = StreamingUiModel(
    id = id,
    name = name,
    priority = priority,
    logoURL = buildImageFullURL(logoPath)
)
