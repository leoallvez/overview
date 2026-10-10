package br.dev.singular.overview.domain.model

sealed interface Prediction {
    data class Available(val affinityPercentage: Int) : Prediction
    data object Unavailable : Prediction

    data object Error : Prediction

    companion object {
        const val MIN_PERCENTAGE = 0
        const val MAX_PERCENTAGE = 100
    }
}
