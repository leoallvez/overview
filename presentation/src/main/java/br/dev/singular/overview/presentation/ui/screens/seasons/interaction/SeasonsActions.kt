package br.dev.singular.overview.presentation.ui.screens.seasons.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
class SeasonsActions(
    private val navigation: INavigationWrapper? = null,
    private val handleIntent: (SeasonsIntent) -> Unit = {}
) {
    val tagPath: String = "/seasons"

    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = TagCommon.Detail.BACK)
        navigation?.popBackStack()
    }

    fun onLoad(tvShowId: Long) = handleIntent(SeasonsIntent.Load(tvShowId))
    fun onSeasonSelected(seasonNumber: Int) = handleIntent(SeasonsIntent.SelectSeason(seasonNumber))
    fun onToggleWatched(episodeNumber: Int) = handleIntent(SeasonsIntent.ToggleEpisodeWatched(episodeNumber))
    fun onMarkSeason() = handleIntent(SeasonsIntent.MarkSeasonWatched)
    fun onRefresh() = handleIntent(SeasonsIntent.Refresh)
}
