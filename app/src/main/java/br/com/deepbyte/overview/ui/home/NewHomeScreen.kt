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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
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
import br.com.deepbyte.overview.data.model.provider.StreamingsWrap
import br.com.deepbyte.overview.data.sampe.slideMediaSample
import br.com.deepbyte.overview.ui.AdsBanner
import br.com.deepbyte.overview.ui.Backdrop
import br.com.deepbyte.overview.ui.BasicImage
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.ScreenTitle
import br.com.deepbyte.overview.ui.SearchField
import br.com.deepbyte.overview.ui.SimpleTitle
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.UiStateResult
import br.com.deepbyte.overview.ui.navigation.wrappers.HomeNavigate
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
import br.com.deepbyte.overview.util.MediaItemClick
import br.com.deepbyte.overview.util.toJson

@Composable
fun NewHomeScreen(
    navigate: HomeNavigate,
    viewModel: NewHomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)
    HomeContent(navigate = navigate, viewModel = viewModel)
}

@Composable
fun HomeContent(
    navigate: HomeNavigate,
    viewModel: NewHomeViewModel
) {
    Scaffold(
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.screen_padding)
        ),
        topBar = {
            Box(
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding))
            ) {
                SearchField(
                    enabled = false,
                    onClick = navigate::toSearch,
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
        UiStateResult(
            uiState = viewModel.uiState.collectAsState().value,
            onRefresh = { viewModel.refresh() }
        ) { data ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                SlideMedia(medias = slideMediaSample, navigate::toMediaDetails)
                StreamingsGrid(wrap = data, onClickItem = navigate::toStreamingExplore)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideMedia(medias: List<Media>, onClickItem: MediaItemClick) {
    if (medias.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { medias.size })
        Box(
            modifier = Modifier
                .background(SecondaryBackground)
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.backdrop_height))
                .padding(bottom = dimensionResource(R.dimen.corner))
                .clickable {
                    val media = medias[pagerState.currentPage]
                    onClickItem(media.apiId, media.getType())
                }
        ) {
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
fun StreamingsGrid(
    wrap: StreamingsWrap,
    onClickItem: (String) -> Unit
) {
    val columns = 4
    val padding = dimensionResource(R.dimen.default_padding)
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columns),
        modifier = Modifier.padding(padding),
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        streamingSession(
            top = { SimpleTitle(title = "Principais Streamings") },
            streamings = wrap.selected,
            columns = columns,
            onClick = onClickItem
        )
        streamingSession(
            top = { SimpleTitle(title = "Demais Streamings") },
            streamings = wrap.unselected,
            columns = columns,
            onClick = onClickItem
        )
    }
}

private fun LazyGridScope.streamingSession(
    top: @Composable () -> Unit,
    streamings: List<Streaming>,
    columns: Int,
    onClick: (String) -> Unit
) {
    if (streamings.isNotEmpty()) {
        item(span = { GridItemSpan(currentLineSpan = columns) }) {
            top()
        }
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
