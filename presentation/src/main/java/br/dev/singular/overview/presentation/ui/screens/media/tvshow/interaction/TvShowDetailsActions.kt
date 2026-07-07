package br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.media.interaction.MediaDetailsActions

@Immutable
class TvShowDetailsActions(
    navigation: INavigationWrapper?,
    val handleIntent: (TvShowDetailsIntent) -> Unit,
) : MediaDetailsActions(tagPath = "/tv-show-details", navigation) {

    fun onLoad(id: Long) =
        handleIntent(TvShowDetailsIntent.Load(id))

    fun onLike(tvShow: MediaDetailsUiModel.TvShow) =
        handleIntent(TvShowDetailsIntent.Like(tvShow))

    fun onSelectCatalog(catalog: CatalogUiModel) {
        handleIntent(TvShowDetailsIntent.SelectCatalog(catalog))
        onToCatalogDetails(catalog.id)
    }
}

@Composable
fun rememberTvShowDetailsActions(
    navigation: INavigationWrapper? = null,
    handleIntent: (TvShowDetailsIntent) -> Unit = {},
) = remember(handleIntent, navigation) {
    TvShowDetailsActions(navigation, handleIntent)
}
