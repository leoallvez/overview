package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable
import kotlin.math.roundToInt

@Immutable
sealed interface EpisodesProgressUiModel {
    @Immutable
    data object Loading : EpisodesProgressUiModel

    @Immutable
    data object SignedOut : EpisodesProgressUiModel

    @Immutable
    data object NotStarted : EpisodesProgressUiModel

    @Immutable
    data class InProgress(
        val progress: Float
    ) : EpisodesProgressUiModel {
        val percentage: Int get() = (progress.coerceIn(0f, 1f) * PERCENT).roundToInt()
    }

    @Immutable
    data object Completed : EpisodesProgressUiModel

    private companion object {
        const val PERCENT = 100
    }
}
