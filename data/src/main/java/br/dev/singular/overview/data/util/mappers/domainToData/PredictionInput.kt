package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.PredictionInputDataModel
import br.dev.singular.overview.domain.model.PredictionInput

internal fun PredictionInput.toData() = PredictionInputDataModel(
    id = id,
    title = title,
    genres = genres
)
