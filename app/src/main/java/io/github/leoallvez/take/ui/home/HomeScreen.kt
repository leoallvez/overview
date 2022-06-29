package io.github.leoallvez.take.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.MediaSuggestion
import io.github.leoallvez.take.ui.*
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
                    HomeToolBar(items = featured)
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

@Composable
fun ErrorOnLoading(refresh: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            modifier = Modifier.border(1.dp, Color.White),
            onClick = { refresh.invoke() },
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp
            )
        ) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = stringResource(id = R.string.refresh_icon),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.btn_try_again))
        }

    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HomeToolBar(
    items: List<MediaItem>
) {
    HorizontalCardSlider(items)
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .road(Alignment.TopEnd, Alignment.TopEnd)
            .padding(end = 15.dp, top = 5.dp)
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.refresh_icon),
            modifier = Modifier.size(30.dp),
            tint = Color.White
        )
    }
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HorizontalCardSlider(
    items: List<MediaItem>
) {

    val pagerState = rememberPagerState(pageCount = items.size)

    Box {
        HorizontalPager(state = pagerState) { page ->
            val item = items[page]
            CardSliderImage(item = item)
            Text(
                text = item.getItemTitle(),
                modifier = Modifier
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.6f))
                    .fillMaxSize()
                    .align(Alignment.BottomEnd)
                    .padding(start = 7.dp, top = 5.dp, bottom = 5.dp),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            inactiveColor = Color.Gray,
            activeColor = Color.White,
        )
    }
}

@Composable
fun CollapsingToolbarScope.CardSliderImage(item: MediaItem) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = item.getItemBackdrop())
            .crossfade(true)
            .build(),
        modifier = Modifier
            .parallax(ratio = 0.2f)
            .background(Color.Black)
            .fillMaxWidth()
            .height(235.dp)
            .pin(),
        contentScale = ContentScale.FillHeight,
        contentDescription = item.getItemTitle(),
    )
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
