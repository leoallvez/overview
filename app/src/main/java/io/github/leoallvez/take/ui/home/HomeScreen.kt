package io.github.leoallvez.take.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.AudioVisual
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.ui.AdsBannerBottomAppBar
import io.github.leoallvez.take.ui.HorizontalAudioVisualCard
import io.github.leoallvez.take.ui.ListTitle
import io.github.leoallvez.take.util.getStringByName

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val suggestions = viewModel.getSuggestions().observeAsState(listOf())
    val showAd = viewModel.adsAreVisible().observeAsState(initial = false)

    Scaffold(
        topBar = {},
        content = {
            HomeScreenContent(suggestions.value)
        },
        bottomBar = {
            AdsBannerBottomAppBar(
                bannerId = R.string.banner_sample_id,
                isVisible = showAd.value
            )
        }
    )
}

@Composable
fun HomeScreenContent(suggestions: List<SuggestionResult>) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .background(Color.Black)
            .padding(10.dp, bottom = 50.dp),
    ) {
        Column {
            SuggestionVerticalList(
                suggestions
            )
        }
    }
}

@Composable
fun SuggestionVerticalList(
    moviesSuggestions: List<SuggestionResult>
) {
    val context = LocalContext.current
    LazyColumn {
        items(moviesSuggestions) {
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