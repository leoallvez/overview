package io.github.leoallvez.take.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.AudioVisual
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.ui.AdsBannerBottomAppBar
import io.github.leoallvez.take.ui.HorizontalAudioVisualCard
import io.github.leoallvez.take.ui.ListTitle
import io.github.leoallvez.take.ui.theme.Teal200
import io.github.leoallvez.take.util.getStringByName
import me.onebone.toolbar.*

@ExperimentalPagerApi
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val suggestions = viewModel.getSuggestions().observeAsState(listOf())
    val showAd = viewModel.adsAreVisible().observeAsState(initial = false)
    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.EnterAlways,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            HomeToolBar(state)
        },
    ) {
        HomeScreenContent(
            suggestions = suggestions.value,
            adsBannerIsVisible = showAd.value
        )
    }
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HomeToolBar(
    state: CollapsingToolbarScaffoldState
) {
    //val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
    val pagerState = rememberPagerState(pageCount = 10)

    HorizontalCardSlider(pagerState)

    Text(
        text = "Take",
        modifier = Modifier
            .road(Alignment.TopEnd, Alignment.TopEnd)
            .padding(10.dp),
        color = Color.White,
        fontSize = 30.sp
    )
}

@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.HorizontalCardSlider(pagerState: PagerState) {

    Box {
        HorizontalPager(state = pagerState) { page ->
            Column {
                Image(
                    modifier = Modifier
                        .parallax(ratio = 0.2f)
                        .background(Color.Black)
                        .fillMaxWidth()
                        .height(235.dp)
                        .pin(),
                    painter = painterResource(id = R.drawable.aranha),
                    contentDescription = null
                )
                Text(
                    color = Color.White,
                    text = "Sample of title with pager: $page",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(10.dp, bottom = 10.dp),
                        //.border(1.dp, color = Color.Yellow),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    //textAlign = TextAlign.Center,
                )
            }
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
fun HomeScreenContent(
    suggestions: List<SuggestionResult>,
    adsBannerIsVisible: Boolean
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .background(Color.Black)
            .padding(10.dp, bottom = 50.dp),
    ) {
        Column {
            AdsBannerBottomAppBar(
                bannerId = R.string.banner_sample_id,
                isVisible = adsBannerIsVisible
            )
            SuggestionVerticalList(
                suggestions = suggestions
            )
        }
    }
}

@Composable
fun SuggestionVerticalList(
    suggestions: List<SuggestionResult>
) {
    val context = LocalContext.current
    LazyColumn {
        items(suggestions) {
            AudioVisualHorizontalList(
                title = context.getStringByName(it.titleResourceId),
                audioVisuals = it.audioVisuals
            )
        }
    }
}

@Composable
fun AudioVisualHorizontalList(
    title: String,
    audioVisuals: List<AudioVisual>
) {
    ListTitle(title)
    LazyRow {
        items(audioVisuals) { audiovisual ->
            HorizontalAudioVisualCard(audiovisual)
        }
    }
}