package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.presentation.model.PredictionUiModel

internal fun Prediction.toUi(): PredictionUiModel = when (this) {
    is Prediction.Available -> PredictionUiModel.Predicted(affinityPercentage)
    is Prediction.Unavailable -> PredictionUiModel.Unavailable
    is Prediction.Error -> PredictionUiModel.Error
}
