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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.R
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.ui.DefaultVerticalSpace
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaEntityPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeSelector
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.search.CenteredTextString
import br.dev.singular.overview.ui.theme.PrimaryBackground

@Composable
fun FavoritesScreen(
    navigate: BasicNavigate,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Favorites, viewModel.analyticsTracker)

    val type = viewModel.mediaType.collectAsState().value
    val items = viewModel.medias.collectAsLazyPagingItems()

    Scaffold(
        containerColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            FavoritesToolBar()
        },
        bottomBar = {
            // TODO: Find a better way to show ads
            // AdsBanner(R.string.liked_banner, viewModel.showAds)
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            MediaTypeSelector(type.key) { newType ->
                viewModel.updateType(newType)
            }
            DefaultVerticalSpace()
            Box {
                when (items.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.NotLoading -> {
                        if (items.itemCount > 0) {
                            MediaEntityPagingVerticalGrid(
                                items = items,
                                onClick = navigate::toMediaDetails
                            )
                        } else {
                            NothingWasFavorite(type)
                        }
                    }
                    else -> NotFoundContentScreen()
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
            .padding(bottom = dimensionResource(R.dimen.screen_padding))
    ) {
        ToolbarTitle(title = stringResource(id = R.string.favorites))
    }
}

@Composable
fun NothingWasFavorite(type: MediaType) {
    CenteredTextString(
        textRes = when (type) {
            MediaType.MOVIE -> R.string.liked_movie_not_found
            MediaType.TV_SHOW -> R.string.liked_tv_show_not_found
            else -> R.string.liked_not_found
        }
    )
}
