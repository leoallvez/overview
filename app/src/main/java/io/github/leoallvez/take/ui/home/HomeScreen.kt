package io.github.leoallvez.take.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.*
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.ui.AdsBannerBottomAppBar
import io.github.leoallvez.take.ui.HorizontalAudioVisualCard
import io.github.leoallvez.take.ui.ListTitle
import io.github.leoallvez.take.util.getStringByName
import me.onebone.toolbar.*

@ExperimentalPagerApi
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

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
                    adsBannerIsVisible = showAd
                )
            }
        } else {
            Toast.makeText(LocalContext.current, "Erro ao carregar...", Toast.LENGTH_LONG).show()
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
                contentDescription = "Favorite",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Tentar novamente")
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
private fun CollapsingToolbarScope.HorizontalCardSlider(
    items: List<MediaItem>
) {

    val pagerState = rememberPagerState(pageCount = items.size)

    Box {
        HorizontalPager(state = pagerState) { page ->
            val item = items[page]
            Column {
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
                    placeholder = painterResource(R.drawable.placeholder),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = item.getItemTitle(),
                )
                Text(
                    color = Color.White,
                    text = item.getItemTitle(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(10.dp, bottom = 10.dp),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                audioVisuals = it.items
            )
        }
    }
}

@Composable
fun AudioVisualHorizontalList(
    title: String,
    audioVisuals: List<MediaItem>
) {
    ListTitle(title)
    LazyRow {
        items(audioVisuals) { audiovisual ->
            HorizontalAudioVisualCard(audiovisual)
        }
    }
}