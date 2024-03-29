package br.dev.singular.overview.ui.liked

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.R
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.ButtonWithIcon
import br.dev.singular.overview.ui.DefaultVerticalSpace
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaEntityPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeSelector
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.search.CenteredTextString
import br.dev.singular.overview.ui.theme.AccentColor
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
            LikedToolBar(navigate::popBackStack)
        },
        bottomBar = {
            AdsBanner(R.string.liked_banner, viewModel.showAds)
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
fun LikedToolBar(toBackStack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.default_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonWithIcon(
            painter = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            descriptionResource = R.string.backstack_icon,
            background = Color.White.copy(alpha = 0.1f),
            padding = PaddingValues(
                vertical = dimensionResource(R.dimen.screen_padding),
                horizontal = 2.dp
            ),
            onClick = toBackStack::invoke,
            onLongClick = toBackStack::invoke
        )
        Text(
            text = stringResource(id = R.string.my_favorite),
            color = AccentColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.screen_padding)
            ),
            overflow = TextOverflow.Ellipsis
        )
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
