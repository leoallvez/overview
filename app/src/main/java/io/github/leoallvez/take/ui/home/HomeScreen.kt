package io.github.leoallvez.take.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.MediaSuggestion
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.Background
import io.github.leoallvez.take.ui.theme.BlueTake
import io.github.leoallvez.take.util.getStringByName
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    nav: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    logger: Logger
) {
    TrackScreenView(screen = Screen.Home, logger)

    val suggestions = viewModel.suggestions.observeAsState(listOf()).value
    val featured = viewModel.featured.observeAsState(listOf()).value
    val loading = viewModel.loading.observeAsState(initial = true).value
    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value

    if(loading) {
        LoadingIndicator()
    } else {
        if (suggestions.isNotEmpty()) {
            CollapsingToolbarScaffold(
                modifier = Modifier,
                scrollStrategy = ScrollStrategy.EnterAlways,
                state = rememberCollapsingToolbarScaffoldState(),
                toolbar = {
                    HomeToolBar(items = featured) { mediaId, mediaType ->
                        nav.navigate(Screen.MediaDetails.editRoute(id = mediaId, type = mediaType))
                    }
                },
            ) {
                HomeScreenContent(suggestions = suggestions, showAds = showAds, nav = nav)
            }
        } else {
            ErrorOnLoading { viewModel.refresh() }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HomeToolBar(
    items: List<MediaItem>,
    callback: (mediaId: Long, mediaType: String?) -> Unit,
) {
    HorizontalCardSlider(items, callback)
    ToolbarButton(
        painter = Icons.Filled.Search,
        descriptionResource = R.string.back_to_home_icon,
        iconTint = BlueTake,
        modifier = Modifier.road(Alignment.TopEnd, Alignment.TopEnd)
    ) { }
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HorizontalCardSlider(
    items: List<MediaItem>,
    callback: (mediaId: Long, mediaType: String?) -> Unit,
) {

    val pagerState = rememberPagerState(pageCount = items.size)

    Box {
        HorizontalPager(state = pagerState) { page ->
            val item = items[page]
            Box(Modifier.clickable { callback.invoke(item.apiId, item.type) }) {
                item.apply {
                    CardSliderImage(item = this)
                    BackdropTitle(
                        text = getItemTitle(),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(dimensionResource(R.dimen.screen_padding)),
            inactiveColor = Color.Gray,
            activeColor = BlueTake,
        )
    }
}

@Composable
fun CollapsingToolbarScope.CardSliderImage(item: MediaItem) {
    item.apply {
        BackdropImage(
            data = getItemBackdrop(),
            contentDescription = getItemBackdrop()
        )
    }
}

@Composable
fun HomeScreenContent(
    suggestions: List<MediaSuggestion>,
    showAds: Boolean,
    nav: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = dimensionResource(R.dimen.default_padding))
    ) {
        Column {
            AdsBanner(bannerId = R.string.banner_sample_id, isVisible = showAds)
            SuggestionVerticalList(nav = nav, suggestions = suggestions)
        }
    }
}

@Composable
fun SuggestionVerticalList(
    nav: NavController,
    suggestions: List<MediaSuggestion>,
) {
    LazyColumn {
        items(suggestions) {
            val title = LocalContext.current.getStringByName(it.titleResourceId)
            MediaItemList(
                listTitle = title, medias = it.items, navigation = nav, mediaType = it.type
            )
        }
    }
}
