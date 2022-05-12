package io.github.leoallvez.take.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.AudioVisual
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.ui.AdsBannerBottomAppBar
import io.github.leoallvez.take.ui.HorizontalAudioVisualCard
import io.github.leoallvez.take.ui.ListTitle
import io.github.leoallvez.take.util.getStringByName
import me.onebone.toolbar.*

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

@Composable
private fun CollapsingToolbarScope.HomeToolBar(
    state: CollapsingToolbarScaffoldState
) {
    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
    Image(
        modifier = Modifier
            .parallax(ratio = 0.2f)
            .background(Color.Black)
            .fillMaxWidth()
            .height(245.dp)
            .pin(),
        painter = painterResource(id = R.drawable.aranha),
        contentDescription = null
    )

    Text(
        text = "Take",
        modifier = Modifier
            .road(Alignment.CenterStart, Alignment.BottomEnd)
            .padding(16.dp),
        color = Color.White,
        fontSize = textSize
    )
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