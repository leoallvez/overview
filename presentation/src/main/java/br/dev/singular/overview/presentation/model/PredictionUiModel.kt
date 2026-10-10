package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable

private const val MIN_PERCENTAGE = 0
private const val MAX_PERCENTAGE = 100
private const val HIGH_AFFINITY_THRESHOLD = 80
private const val MEDIUM_AFFINITY_THRESHOLD = 50

enum class AffinityLevel {
    HIGH,
    MEDIUM,
    LOW
}

@Immutable
sealed interface PredictionUiModel {
    @Immutable
    data object Loading : PredictionUiModel

    @Immutable
    data object Idle : PredictionUiModel

    @Immutable
    data class Predicted(
        val affinityPercentage: Int
    ) : PredictionUiModel {

        init {
            require(affinityPercentage in MIN_PERCENTAGE..MAX_PERCENTAGE) {
                "affinityPercentage must be between $MIN_PERCENTAGE and $MAX_PERCENTAGE"
            }
        }

        val progress: Float
            get() = affinityPercentage / MAX_PERCENTAGE.toFloat()

        val level: AffinityLevel
            get() = when {
                affinityPercentage >= HIGH_AFFINITY_THRESHOLD -> AffinityLevel.HIGH
                affinityPercentage >= MEDIUM_AFFINITY_THRESHOLD -> AffinityLevel.MEDIUM
                else -> AffinityLevel.LOW
            }
    }

    @Immutable
    data object Unavailable : PredictionUiModel

    @Immutable
    data object LoginRequired : PredictionUiModel

    @Immutable
    data object Error : PredictionUiModel
}
