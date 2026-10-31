package br.dev.singular.overview.presentation.ui.screens.episodes.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
class EpisodesActions(
    private val navigation: INavigationWrapper? = null
) {
    val tagPath: String = "/episodes"

    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = TagCommon.Detail.BACK)
        navigation?.popBackStack()
    }
}
