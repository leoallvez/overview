package br.dev.singular.overview.presentation.ui.screens.media.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.navigation.Destination
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper

@Immutable
abstract class MediaDetailsActions(
    val tagPath: String,
    protected val navigation: INavigationWrapper?,
    private val onShowSnackbar: (UiSnackbarVisuals) -> Unit = {},
) {
    abstract fun onSelectCatalog(catalog: CatalogUiModel)

    abstract fun onFavoriteAdded(visuals: UiSnackbarVisuals)

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

    protected fun onToLogin() {
        TagManager.logClick(customPath = tagPath, detail = "login-required-confirm")
        navigation?.navigate(route = Destination.Favorites.route)
    }

    protected fun onShowFavoriteAdded(visuals: UiSnackbarVisuals) {
        TagManager.logInteraction(customPath = tagPath, detail = "favorite-added")
        onShowSnackbar(visuals)
    }

    protected fun onLoginCancel() {
        TagManager.logClick(customPath = tagPath, detail = "login-required-cancel")
    }

    protected fun onToCatalogDetails(id: Long) {
        TagManager.logClick(customPath = tagPath, detail = "catalog", id = id)
        navigation?.toHome()
    }
}
