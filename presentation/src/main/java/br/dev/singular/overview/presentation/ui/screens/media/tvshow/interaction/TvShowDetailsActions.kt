package br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.media.interaction.MediaDetailsActions

@Immutable
class TvShowDetailsActions(
    navigation: INavigationWrapper?,
    val handleIntent: (TvShowDetailsIntent) -> Unit,
    onShowSnackbar: (UiSnackbarVisuals) -> Unit = {},
) : MediaDetailsActions(tagPath = "/tv-show-details", navigation, onShowSnackbar) {

    fun onLoad(id: Long) =
        handleIntent(TvShowDetailsIntent.Load(id))

    fun onLike(tvShow: MediaDetailsUiModel.TvShow) =
        handleIntent(TvShowDetailsIntent.Like(tvShow))

    override fun onFavoriteAdded(visuals: UiSnackbarVisuals) {
        onShowFavoriteAdded(visuals)
        handleIntent(TvShowDetailsIntent.DismissFavoriteAdded)
    }

    fun onLoginConfirm() {
        handleIntent(TvShowDetailsIntent.DismissLoginAlert)
        onToLogin()
    }

    fun onLoginDismiss() {
        onLoginCancel()
        handleIntent(TvShowDetailsIntent.DismissLoginAlert)
    }

    override fun onSelectCatalog(catalog: CatalogUiModel) {
        handleIntent(TvShowDetailsIntent.SelectCatalog(catalog))
        onToCatalogDetails(catalog.id)
    }
}

@Composable
fun rememberTvShowDetailsActions(
    navigation: INavigationWrapper? = null,
    handleIntent: (TvShowDetailsIntent) -> Unit = {},
    onShowSnackbar: (UiSnackbarVisuals) -> Unit = {},
) = remember(handleIntent, navigation, onShowSnackbar) {
    TvShowDetailsActions(navigation, handleIntent, onShowSnackbar)
}
