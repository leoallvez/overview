package br.dev.singular.overview.presentation.ui.components.prediction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.model.PredictionUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun rememberAiPredictionViewModel(): IAiPredictionViewModel =
    if (LocalInspectionMode.current) {
        remember { PreviewAiPredictionViewModel(MutableStateFlow(PredictionUiModel.Idle)) }
    } else {
        hiltViewModel<AiPredictionViewModel>()
    }

internal class PreviewAiPredictionViewModel(
    override val uiState: StateFlow<PredictionUiModel>,
) : IAiPredictionViewModel {
    override fun onPredict(model: MediaDetailsUiModel) = Unit
}

internal val previewStates = listOf(
    PredictionUiModel.Idle,
    PredictionUiModel.Loading,
    PredictionUiModel.Predicted(affinityPercentage = 90),
    PredictionUiModel.Predicted(affinityPercentage = 50),
    PredictionUiModel.Predicted(affinityPercentage = 10),
    PredictionUiModel.Unavailable,
    PredictionUiModel.Error,
)
