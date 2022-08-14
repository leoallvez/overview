package io.github.leoallvez.take.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    val loading = viewModel.loading.observeAsState(true).value
    val showAd = viewModel.adsAreVisible().observeAsState(initial = false).value

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
                HomeScreenContent(
                    suggestions = suggestions,
                    adsBannerIsVisible = showAd,
                    nav = nav,
                )
            }
        } else {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.error_on_loading),
                Toast.LENGTH_LONG
            ).show()
            ErrorOnLoading(refresh = { viewModel.refresh() })
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
        modifier = Modifier
            .road(Alignment.TopEnd, Alignment.TopEnd)
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
                .padding(16.dp),
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
    adsBannerIsVisible: Boolean,
    nav: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .background(Color.Black)
    ) {
        Column {
            val adPadding = 10.dp
            AdsBanner(
                bannerId = R.string.banner_sample_id,
                isVisible = adsBannerIsVisible,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = adPadding, bottom = adPadding)
                    .height(50.dp)
            )
            SuggestionVerticalList(
                nav = nav,
                suggestions = suggestions
            )
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
            MediaHorizontalList(
                title = LocalContext.current.getStringByName(it.titleResourceId),
                mediaType = it.type,
                nav = nav,
                items = it.items,
            )
        }
    }
}

@Composable
fun MediaHorizontalList(
    title: String,
    mediaType: String,
    nav: NavController,
    items: List<MediaItem>,
) {
    ListTitle(title)
    LazyRow {
        items(items) { item ->
            MediaCard(item) { mediaId: Long ->
                nav.navigate(Screen.MediaDetails.editRoute(id = mediaId, type = mediaType))
            }
        }
    }
}
