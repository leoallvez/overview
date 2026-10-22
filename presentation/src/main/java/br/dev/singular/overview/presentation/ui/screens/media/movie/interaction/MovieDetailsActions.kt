package br.dev.singular.overview.presentation.ui.screens.media.movie.interaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.media.interaction.MediaDetailsActions

@Immutable
class MovieDetailsActions(
    navigation: INavigationWrapper?,
    val handleIntent: (MovieDetailsIntent) -> Unit,
    onShowSnackbar: (UiSnackbarVisuals) -> Unit = {},
) : MediaDetailsActions(tagPath = "/movie-details", navigation, onShowSnackbar) {

    fun onLoad(id: Long) =
        handleIntent(MovieDetailsIntent.Load(id))

    fun onLike(movie: MediaDetailsUiModel.Movie) =
        handleIntent(MovieDetailsIntent.Like(movie))

    override fun onFavoriteAdded(visuals: UiSnackbarVisuals) {
        onShowFavoriteAdded(visuals)
        handleIntent(MovieDetailsIntent.DismissFavoriteAdded)
    }

    fun onLoginConfirm() {
        handleIntent(MovieDetailsIntent.DismissLoginAlert)
        onToLogin()
    }

    fun onLoginDismiss() {
        onLoginCancel()
        handleIntent(MovieDetailsIntent.DismissLoginAlert)
    }

    override fun onSelectCatalog(catalog: CatalogUiModel) {
        onToCatalogDetails(catalog.id)
        handleIntent(MovieDetailsIntent.SelectCatalog(catalog))
    }
}

@Composable
fun rememberMovieDetailsActions(
    navigation: INavigationWrapper? = null,
    handleIntent: (MovieDetailsIntent) -> Unit = {},
    onShowSnackbar: (UiSnackbarVisuals) -> Unit = {},
) = remember(handleIntent, navigation, onShowSnackbar) {
    MovieDetailsActions(navigation, handleIntent, onShowSnackbar)
}
