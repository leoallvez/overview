package br.dev.singular.overview.presentation.ui.screens.media.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
open class MediaDetailsActions(
    val tagPath: String,
    protected val navigation: INavigationWrapper?,
) {
    fun onBack() {
        TagManager.logClick(customPath = tagPath, detail = TagCommon.Detail.BACK)
        navigation?.popBackStack()
    }

    fun onToMediaDetails(media: MediaUiModel) {
        TagMediaManager.logMediaClick(tagPath, media.id)
        navigation?.toMediaDetails(media)
    }

    fun onToPersonDetails(id: Long) {
        TagManager.logClick(customPath = tagPath, detail = "cast", id = id)
        navigation?.navigate(route = Destination.PersonDetails.editRoute(id))
    }

    fun onToVideoPlayer(videoKey: String) {
        TagManager.logClick(customPath = tagPath, detail = "video")
        navigation?.navigate(route = Destination.YouTubePlayer.editRoute(videoKey))
    }

    protected fun onToCatalogDetails(id: Long) {
        TagManager.logClick(customPath = tagPath, detail = "catalog", id = id)
        navigation?.toHome()
    }
}
