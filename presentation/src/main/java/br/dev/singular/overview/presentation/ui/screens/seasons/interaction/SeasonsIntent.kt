package br.dev.singular.overview.presentation.ui.screens.seasons.interaction

sealed class SeasonsIntent {
    data class Load(val tvShowId: Long) : SeasonsIntent()
    data object Refresh : SeasonsIntent()
    data class SelectSeason(val seasonNumber: Int) : SeasonsIntent()
    data class ToggleEpisodeWatched(val episodeNumber: Int) : SeasonsIntent()
    data object MarkSeasonWatched : SeasonsIntent()
}
