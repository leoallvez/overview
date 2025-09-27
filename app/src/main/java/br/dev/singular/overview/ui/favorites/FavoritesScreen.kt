package br.dev.singular.overview.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagFavorites
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.ui.DefaultVerticalSpace
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaGrid
import br.dev.singular.overview.ui.MediaTypeSelector
import br.dev.singular.overview.ui.NothingFoundScreen
import br.dev.singular.overview.ui.TagScreenView
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.search.CenteredTextString
import br.dev.singular.overview.ui.theme.PrimaryBackground

private fun tagClick(detail: String, id: Long = 0L) {
    TagManager.logClick(TagFavorites.PATH, detail, id)
}

@Composable
fun FavoritesScreen(
    navigate: BasicNavigate,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val type = viewModel.mediaType.collectAsState().value
    val items = viewModel.medias.collectAsLazyPagingItems()

    Scaffold(
        containerColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.spacing_small)),
        topBar = {
            FavoritesToolBar()
        }
    ) { padding ->
        Column(Modifier.padding(top = padding.calculateTopPadding())) {
            MediaTypeSelector(type.key) { newType ->
                tagClick("${TagMediaManager.Detail.SELECT_MEDIA_TYPE}${newType.key}")
                viewModel.updateType(newType)
            }
            DefaultVerticalSpace()
            Box {
                when (items.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen(TagFavorites.PATH)
                    is LoadState.NotLoading -> {
                        if (items.itemCount > 0) {
                            MediaGrid(
                                items = items,
                                tagPath = TagFavorites.PATH,
                                onClick = { id: Long, type: String? ->
                                    navigate.toMediaDetails(id, type)
                                }
                            )
                        } else {
                            NothingWasFavorite(type)
                        }
                    }
                    else -> NothingFoundScreen(TagFavorites.PATH)
                }
            }
        }
    }
}

@Composable
fun FavoritesToolBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.spacing_small))
    ) {
        ToolbarTitle(title = stringResource(id = R.string.favorites))
    }
}

@Composable
fun NothingWasFavorite(type: MediaType) {
    TagScreenView(TagFavorites.PATH, TagStatus.NOTHING_FOUND)
    CenteredTextString(
        textRes = when (type) {
            MediaType.MOVIE -> R.string.liked_movie_not_found
            MediaType.TV_SHOW -> R.string.liked_tv_show_not_found
            else -> R.string.liked_not_found
        }
    )
}
