package io.github.leoallvez.take.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.leoallvez.take.R
import io.github.leoallvez.take.ui.AdsBanner
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import io.github.leoallvez.take.data.model.AudioVisual
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.util.getStringByName

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val suggestions = viewModel.getSuggestions().observeAsState(listOf()).value
    val showAd = viewModel.adsAreVisible().observeAsState(initial = false)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .padding(10.dp),
    ) {
        Column {
            AdsBanner(
                bannerId = R.string.banner_sample_id,
                isVisible = false //showAd.value
            )
            Spacer(modifier = Modifier.padding(end = 10.dp))
            SuggestionVerticalList(
                moviesSuggestions = suggestions
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
            AudioVisualCard(audiovisual)
        }
    }
}

@Composable
fun ListTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(start = 5.dp, bottom = 5.dp, top = 20.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun AudioVisualCard(audioVisual: AudioVisual) {
    Column(
        modifier = Modifier
            .size(width = 150.dp, height = 260.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            contentColor = Color.Black,
            elevation = 7.dp,
            modifier = Modifier
                .padding(5.dp)
        ) {
            AsyncImage(
                model = audioVisual.getImageUrl(),
                contentDescription = audioVisual.getContentTitle(),
            )
        }
        Text(
            text = audioVisual.getContentTitle(),
            modifier = Modifier.padding(4.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

