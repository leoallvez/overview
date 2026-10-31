package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class SeasonUiModel(
    val tvShowTitle: String,
    val numberOfSeasons: Int,
    val selectedSeason: Int,
    val episodes: ImmutableList<EpisodeUiModel>,
    val watchedEpisodes: ImmutableList<Int>,
    val progressText: String,
    val progressPercentage: Float,
    val isEpisodesLoading: Boolean = false
)
