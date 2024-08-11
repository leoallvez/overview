package br.dev.singular.overview.ui.liked

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
fun LikedScreen(
    navigate: BasicNavigate,
    viewModel: LikedViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Liked, viewModel.analyticsTracker)

    val mediaType = viewModel.mediaType.collectAsState().value
    val items = viewModel.medias.collectAsLazyPagingItems()

    Scaffold(
        containerColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            LikedToolBar()
        },
        bottomBar = {
            // TODO: Find a better way to show ads
            // AdsBanner(R.string.liked_banner, viewModel.showAds)
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            MediaTypeSelector(mediaType.key) { newType ->
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
                            NothingLiked(mediaType)
                        }
                    }
                    else -> NotFoundContentScreen()
                }
            }
        }
    }
}

@Composable
fun LikedToolBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.screen_padding)),
    ) {
        ToolbarTitle(title = stringResource(id = R.string.favorites))
    }
}

@Composable
fun NothingLiked(type: MediaType) {
    CenteredTextString(
        textRes = when (type) {
            MediaType.MOVIE -> R.string.liked_movie_not_found
            MediaType.TV_SHOW -> R.string.liked_tv_show_not_found
            else -> R.string.liked_not_found
        }
    )
}
