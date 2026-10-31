package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class EpisodesSummaryUiModel(
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
    val isReleased: Boolean,
    val releaseDate: String,
)
