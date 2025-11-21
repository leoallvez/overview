package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.domain.model.Suggestion

internal fun Suggestion.toData() = SuggestionDataModel(
    id = id,
    order = order,
    type = type.toData(),
    sourceKey = key,
    isActive = isActive,
    lastUpdate = lastUpdate
)
