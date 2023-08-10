package br.com.deepbyte.overview.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.ui.AdsBanner
import br.com.deepbyte.overview.ui.Backdrop
import br.com.deepbyte.overview.ui.BasicImage
import br.com.deepbyte.overview.ui.ErrorScreen
import br.com.deepbyte.overview.ui.LoadingScreen
import br.com.deepbyte.overview.ui.MediaItemList
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.ToolbarButton
import br.com.deepbyte.overview.ui.ToolbarTitle
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.navigation.wrappers.HomeNavigation
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.util.defaultBackground
import br.com.deepbyte.overview.util.defaultBorder
import br.com.deepbyte.overview.util.getStringByName
import br.com.deepbyte.overview.util.toJson
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    navigation: HomeNavigation,
    viewModel: HomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)

    val streamings = viewModel.streaming.collectAsState(listOf()).value
    val suggestions = viewModel.suggestions.observeAsState(listOf()).value
    val featuredMediaItems = viewModel.featuredMediaItems.observeAsState(listOf()).value
    val loading = viewModel.loading.observeAsState(initial = true).value

    if (loading) {
        LoadingScreen()
    } else {
        if (suggestions.isNotEmpty()) {
            CollapsingToolbarScaffold(
                modifier = Modifier,
                scrollStrategy = ScrollStrategy.EnterAlways,
                state = rememberCollapsingToolbarScaffoldState(),
                toolbar = {
                    HomeToolBar(
                        items = featuredMediaItems,
                        onNavigateToSearch = { navigation.toSearch() }
                    ) { item ->
                        navigation.toMediaDetails(item.apiId, item.type)
                    }
                }
            ) {
                HomeScreenContent(
                    showAds = viewModel.showAds,
                    navigation = navigation,
                    suggestions = suggestions,
                    streamings = streamings
                )
            }
        } else {
            ErrorScreen { viewModel.refresh() }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HomeToolBar(
    items: List<MediaItem>,
    onNavigateToSearch: () -> Unit,
    callback: (MediaItem) -> Unit
) {
    HorizontalCardSlider(items, callback)
    ToolbarButton(
        painter = Icons.Filled.Search,
        descriptionResource = R.string.search_icon,
        iconTint = AccentColor,
        modifier = Modifier.road(Alignment.TopEnd, Alignment.TopEnd)
    ) { onNavigateToSearch.invoke() }
}

@ExperimentalPagerApi
@Composable
private fun HorizontalCardSlider(
    items: List<MediaItem>,
    callback: (MediaItem) -> Unit
) {
    val pagerState = rememberPagerState()

    Column(Modifier.background(PrimaryBackground)) {
        Box {
            SlideImage(pagerState = pagerState, items = items, onClick = callback)
            SlideIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun SlideImage(
    pagerState: PagerState,
    items: List<MediaItem>,
    onClick: (MediaItem) -> Unit
) {
    HorizontalPager(state = pagerState, count = items.size) { page ->
        val item = items[page]
        Box(
            Modifier
                .height(dimensionResource(R.dimen.backdrop_height))
                .clickable { onClick.invoke(item) }
        ) {
            Backdrop(
                url = item.getBackdropImage(),
                contentDescription = item.getLetter(),
                modifier = Modifier.align(Alignment.TopCenter)
            )
            ToolbarTitle(
                title = item.getLetter(),
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun SlideIndicator(pagerState: PagerState, modifier: Modifier) {
    HorizontalPagerIndicator(
        pagerState = pagerState,
        modifier = modifier
            .padding(dimensionResource(R.dimen.screen_padding)),
        inactiveColor = Gray,
        activeColor = AccentColor
    )
}

@Composable
fun HomeScreenContent(
    showAds: Boolean,
    navigation: HomeNavigation,
    streamings: List<Streaming>,
    suggestions: List<MediaSuggestion>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
    ) {
        Column {
            AdsBanner(prodBannerId = R.string.home_banner, isVisible = showAds)
            HomeLists(navigation = navigation, suggestions = suggestions, streamings = streamings)
        }
    }
}

@Composable
fun HomeLists(
    navigation: HomeNavigation,
    streamings: List<Streaming>,
    suggestions: List<MediaSuggestion>
) {
    LazyColumn(modifier = Modifier.padding(bottom = 50.dp)) {
        item {
            OverviewStreaming(
                streamings = streamings,
                onItemClick = navigation::toStreamingExplore,
                onEditClick = navigation::toStreamingExploreEdit
            )
        }
        items(suggestions) {
            val title = LocalContext.current.getStringByName(it.titleResourceId)
            MediaItemList(
                listTitle = title ?: "",
                items = it.items,
                mediaType = it.type
            ) { apiId, mediaType ->
                navigation.toMediaDetails(apiId, mediaType)
            }
        }
    }
}

@Composable
fun OverviewStreaming(
    streamings: List<Streaming>,
    onItemClick: (String) -> Unit,
    onEditClick: () -> Unit
) {
    if (streamings.isNotEmpty()) {
        Column(Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding))) {
            OverviewStreamingTitle()
            LazyRow(
                horizontalArrangement = Arrangement
                    .spacedBy(dimensionResource(R.dimen.default_padding)),
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.screen_padding)
                )
            ) {
                items(streamings) { streaming ->
                    StreamingItem(streaming, onItemClick)
                }
                item {
                    EditItem(onEditClick)
                }
            }
        }
    }
}

@Composable
fun OverviewStreamingTitle() {
    Text(
        text = stringResource(R.string.yours_streaming),
        color = Color.White,
        modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding)),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun StreamingItem(streaming: Streaming, onClick: (String) -> Unit) {
    BasicImage(
        url = streaming.getLogoImage(),
        contentDescription = streaming.name,
        withBorder = true,
        modifier = Modifier
            .size(dimensionResource(R.dimen.streaming_item_lager_size))
            .clickable { onClick.invoke(streaming.toJson()) }
    )
}

@Composable
fun EditItem(onClick: () -> Unit) {
    val color = AccentColor
    Box(
        Modifier
            .size(dimensionResource(R.dimen.streaming_item_lager_size))
            .defaultBackground()
            .clickable { onClick.invoke() }
            .defaultBorder(color = color)
    ) {
        Column(Modifier.align(Alignment.Center)) {
            Icon(
                Icons.Default.Edit,
                tint = color,
                contentDescription = stringResource(R.string.edit),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.edit),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = color,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
