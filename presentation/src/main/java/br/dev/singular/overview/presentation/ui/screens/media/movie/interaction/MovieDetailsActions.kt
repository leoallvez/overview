package br.dev.singular.overview.presentation.ui.screens.media.movie.interaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.media.interaction.MediaDetailsActions

@Immutable
class MovieDetailsActions(
    navigation: INavigationWrapper?,
    val handleIntent: (MovieDetailsIntent) -> Unit,
) : MediaDetailsActions(tagPath = "/movie-details", navigation) {

    fun onLoad(id: Long) =
        handleIntent(MovieDetailsIntent.Load(id))

    fun onLike(movie: MediaDetailsUiModel.Movie) =
        handleIntent(MovieDetailsIntent.Like(movie))

    fun onSelectCatalog(catalog: CatalogUiModel) {
        onToCatalogDetails(catalog.id)
        handleIntent(MovieDetailsIntent.SelectCatalog(catalog))
    }
}

@Composable
fun rememberMovieDetailsActions(
    navigation: INavigationWrapper? = null,
    handleIntent: (MovieDetailsIntent) -> Unit = {},
) = remember(handleIntent, navigation) {
    MovieDetailsActions(navigation, handleIntent)
}
