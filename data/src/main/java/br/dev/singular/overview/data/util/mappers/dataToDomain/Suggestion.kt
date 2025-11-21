package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.domain.model.Suggestion

internal fun SuggestionDataModel.toDomain() = Suggestion(
    id = id,
    key = sourceKey,
    order = order,
    type = type.toDomain(),
    isActive = isActive,
    lastUpdate = lastUpdate
)
