package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.Prediction.Companion.MAX_PERCENTAGE
import br.dev.singular.overview.domain.model.Prediction.Companion.MIN_PERCENTAGE

internal fun PredictionDataModel.toDomain(): Prediction {
    val percentage = affinityPercentage ?: return Prediction.Error
    return Prediction.Available(
        affinityPercentage = percentage.coerceIn(MIN_PERCENTAGE, MAX_PERCENTAGE)
    )
}
