package br.com.deepbyte.overview.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.sampe.slideMediaSample
import br.com.deepbyte.overview.ui.AdsBanner
import br.com.deepbyte.overview.ui.Backdrop
import br.com.deepbyte.overview.ui.BasicImage
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.ScreenTitle
import br.com.deepbyte.overview.ui.SearchField
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.UiStateResult
import br.com.deepbyte.overview.ui.navigation.events.HomeScreenEvents
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
import br.com.deepbyte.overview.util.toJson

@Composable
fun NewHomeScreen(
    events: HomeScreenEvents,
    viewModel: NewHomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)

    HomeContent(events = events, viewModel = viewModel)
}

@Composable
fun HomeContent(
    events: HomeScreenEvents,
    viewModel: NewHomeViewModel
) {
    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        topBar = {
            Box(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.screen_padding))
            ) {
                SearchField(
                    enabled = false,
                    onClick = {},
                    defaultPaddingValues = PaddingValues(),
                    placeholder = stringResource(R.string.search_in_all_places)
                )
            }
        },
        bottomBar = {
            AdsBanner(R.string.home_banner, isVisible = viewModel.showAds)
        },
        backgroundColor = PrimaryBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .padding(top = 20.dp)
        ) {
            UiStateResult(
                uiState = viewModel.uiState.collectAsState().value,
                onRefresh = { viewModel.refresh() }
            ) { data ->
                Column {
                    SlideMedia(medias = slideMediaSample)
                    StreamingVerticalGrid(
                        streamings = data.selected,
                        onClick = events::onNavigateToStreamingOverview
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideMedia(medias: List<Media>) {
    if (medias.isNotEmpty()) {
        Box(
            modifier = Modifier
                .background(SecondaryBackground)
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.backdrop_height))
        ) {
            val pagerState = rememberPagerState(pageCount = { medias.size })
            HorizontalPager(
                state = pagerState
            ) { page ->
                Backdrop(
                    url = medias[page].getBackdropImage(),
                    contentDescription = medias[page].getLetter()
                )
            }
            SlideMediaIndicator(
                modifier = Modifier
                    .height(25.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = dimensionResource(R.dimen.corner)),
                pagerState = pagerState
            )
            SlideMediaTitle(
                title = medias[pagerState.currentPage].getLetter(),
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideMediaIndicator(modifier: Modifier, pagerState: PagerState) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                AccentColor
            } else {
                Gray
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(7.dp)

            )
        }
    }
}

@Composable
fun SlideMediaTitle(title: String, modifier: Modifier = Modifier, textPadding: Dp = 0.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, PrimaryBackground)
                )
            )
    ) {
        ScreenTitle(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(textPadding)
        )
    }
}

@Composable
fun StreamingVerticalGrid(
    streamings: List<Streaming>,
    onClick: (String) -> Unit
) {
    val cellSpace = dimensionResource(id = R.dimen.default_padding)
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        modifier = Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(cellSpace),
        horizontalArrangement = Arrangement.spacedBy(cellSpace)
    ) {
        items(streamings.size) { index ->
            HomeStreamingItem(streaming = streamings[index], onClick = onClick)
        }
    }
}

@Composable
fun HomeStreamingItem(
    streaming: Streaming,
    onClick: (String) -> Unit
) {
    BasicImage(
        url = streaming.getLogoImage(),
        contentDescription = streaming.name,
        contentScale = ContentScale.FillBounds,
        withBorder = true,
        modifier = Modifier
            .size(85.dp)
            .clickable { onClick.invoke(streaming.toJson()) }
    )
}
