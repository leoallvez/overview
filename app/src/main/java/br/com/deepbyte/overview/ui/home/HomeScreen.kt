package br.com.deepbyte.overview.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.util.MediaItemClick
import br.com.deepbyte.overview.util.getStringByName
import com.google.accompanist.pager.*
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToMediaDetails: MediaItemClick,
    onNavigateToSearch: () -> Unit
) {

    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)

    val suggestions = viewModel.suggestions.observeAsState(listOf()).value
    val featuredMediaItems = viewModel.featuredMediaItems.observeAsState(listOf()).value
    val loading = viewModel.loading.observeAsState(initial = true).value
    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value

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
                        onNavigateToSearch = { onNavigateToSearch.invoke() }
                    ) { item ->
                        onNavigateToMediaDetails.invoke(item.apiId, item.type)
                    }
                }
            ) {
                HomeScreenContent(
                    suggestions = suggestions,
                    showAds = showAds,
                    onNavigateToMediaDetails = onNavigateToMediaDetails
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
        descriptionResource = R.string.back_to_home_icon,
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
            ScreenTitle(
                text = items[pagerState.currentPage].getLetter(),
                maxLines = 1,
                modifier = Modifier.align(Alignment.BottomStart)
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
                .height(320.dp)
                .clickable { onClick.invoke(item) }
        ) {
            Backdrop(
                url = item.getBackdropImage(),
                contentDescription = item.getLetter(),
                modifier = Modifier.align(Alignment.TopCenter)
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
        inactiveColor = Color.Gray,
        activeColor = AccentColor
    )
}

@Composable
fun HomeScreenContent(
    suggestions: List<MediaSuggestion>,
    showAds: Boolean,
    onNavigateToMediaDetails: MediaItemClick
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
    ) {
        Column {
            AdsBanner(prodBannerId = R.string.banner_sample_id, isVisible = showAds)
            SuggestionVerticalList(suggestions = suggestions) { apiId, mediaType ->
                onNavigateToMediaDetails.invoke(apiId, mediaType)
            }
        }
    }
}

@Composable
fun SuggestionVerticalList(
    suggestions: List<MediaSuggestion>,
    onClickItem: MediaItemClick
) {
    LazyColumn {
        items(suggestions) {
            val title = LocalContext.current.getStringByName(it.titleResourceId)
            MediaItemList(
                listTitle = title, items = it.items, mediaType = it.type
            ) { apiId, mediaType ->
                onClickItem.invoke(apiId, mediaType)
            }
        }
    }
}
