package br.dev.singular.overview.presentation.ui.screens.video.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
data class YouTubePlayerActions(
    private val navigation: INavigationWrapper? = null
) {
    val tagPath: String = "/player-fullscreen:youtube"

    fun onBack() {
        TagManager.logClick(tagPath, TagCommon.Detail.CLOSE)
        navigation?.popBackStack()
    }

    fun onPlayerStateChange(state: String) {
        TagManager.logInteraction(
            customPath = tagPath,
            detail = "player-status:$state"
        )
    }
}
