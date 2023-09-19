package br.dev.singular.overview.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.R
import br.dev.singular.overview.data.model.HomeData
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.Backdrop
import br.dev.singular.overview.ui.BasicImage
import br.dev.singular.overview.ui.STREAMING_GRID_COLUMNS
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SearchField
import br.dev.singular.overview.ui.SimpleTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.navigation.wrappers.HomeNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.MediaItemClick
import br.dev.singular.overview.util.toJson

@Composable
fun HomeScreen(
    navigate: HomeNavigate,
    viewModel: HomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)
    HomeContent(navigate = navigate, viewModel = viewModel)
}

@Composable
fun HomeContent(
    navigate: HomeNavigate,
    viewModel: HomeViewModel
) {
    Scaffold(
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.screen_padding_new)
        ),
        topBar = {
            SearchField(
                Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)),
                enabled = false,
                onClick = navigate::toSearch,
                defaultPaddingValues = PaddingValues(),
                placeholder = stringResource(R.string.search_in_all_places)
            )
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
                StreamingsGrid(
                    homeData = data,
                    header = {
                        SlideRecommendedMedia(data.recommendedMedias, navigate::toMediaDetails)
                    },
                    onClickStreamingItem = navigate::toStreamingExplore,
                    onClickEditStreaming = navigate::toStreamingExploreEdit
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideRecommendedMedia(medias: List<MediaEntity>, onClickItem: MediaItemClick) {
    if (medias.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { medias.size })
        val media = medias[pagerState.currentPage]
        Column {
            SlideMediaTitle(title = media.letter)
            Box(
                modifier = Modifier
                    .background(PrimaryBackground)
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.backdrop_height))
                    .padding(bottom = 20.dp)
                    .clickable { onClickItem(media.apiId, media.type) }
            ) {
                HorizontalPager(state = pagerState) {
                    Backdrop(
                        url = media.getBackdropImage(),
                        contentDescription = media.letter
                    )
                }
                SlideMediaIndicator(
                    modifier = Modifier
                        .height(20.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    pagerState = pagerState
                )
            }
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
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun SlideMediaTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.screen_padding))
    ) {
        Text(
            text = title,
            color = AccentColor,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StreamingsGrid(
    header: @Composable () -> Unit = {},
    homeData: HomeData,
    onClickStreamingItem: (String) -> Unit,
    onClickEditStreaming: () -> Unit
) {
    val padding = dimensionResource(R.dimen.default_padding)
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = STREAMING_GRID_COLUMNS),
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        item(span = { GridItemSpan(currentLineSpan = STREAMING_GRID_COLUMNS) }) {
            header()
        }
        streamingSession(
            top = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    SimpleTitle(title = stringResource(R.string.favorite_streams))
                    EditStreamingText(onClick = onClickEditStreaming)
                }
            },
            streamings = homeData.streams.selected,
            onClick = onClickStreamingItem
        )
        streamingSession(
            top = { SimpleTitle(title = stringResource(R.string.other_streams)) },
            streamings = homeData.streams.unselected,
            onClick = onClickStreamingItem
        )
    }
}

@Composable
fun EditStreamingText(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.edit),
        color = AccentColor,
        modifier = Modifier
            .clickable { onClick.invoke() }
            .padding(bottom = 5.dp, top = 10.dp),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}

private fun LazyGridScope.streamingSession(
    top: @Composable () -> Unit,
    streamings: List<StreamingEntity>,
    onClick: (String) -> Unit
) {
    if (streamings.isNotEmpty()) {
        item(span = { GridItemSpan(currentLineSpan = STREAMING_GRID_COLUMNS) }) {
            top()
        }
        items(streamings.size) { index ->
            HomeStreamingItem(streaming = streamings[index], onClick = onClick)
        }
    }
}

@Composable
fun HomeStreamingItem(
    streaming: StreamingEntity,
    onClick: (String) -> Unit
) {
    BasicImage(
        url = streaming.getLogoImage(),
        contentDescription = streaming.name,
        contentScale = ContentScale.FillBounds,
        withBorder = true,
        modifier = Modifier
            .size(dimensionResource(R.dimen.streaming_item_big_size))
            .clickable { onClick.invoke(streaming.toJson()) }
    )
}
