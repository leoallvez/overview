package io.github.leoallvez.take.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.AudioVisual
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.ui.AdsBanner
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
            .background(Color.Black)
            .padding(10.dp),
    ) {
        Column {
            AdsBanner(
                bannerId = R.string.banner_sample_id,
                isVisible = false//showAd.value
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
        color = Color(0xFFFFB400),
        modifier = Modifier
            .padding(start = 5.dp, bottom = 5.dp, top = 15.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}
//TODO: refactor
@Composable
fun AudioVisualCard(audioVisual: AudioVisual) {
    val with = 140.dp
    Column(
        modifier = Modifier
            .size(width = 140.dp, height = 265.dp).padding(5.dp)
    ) {
        audioVisual.apply {
            AudioVisualImage(
                imageUrl = getImageUrl(),
                contentDescription = getContentTitle(),
                modifier = Modifier
                    .size(
                        width = with,
                        height = 200.dp
                    ).border(1.dp, color = Color.Yellow),
            )
            AudioVisualTitle(title = getContentTitle(), size = with)
        }
    }
}

@Composable
fun AudioVisualImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        Card(
            shape = RoundedCornerShape(6.dp),
            contentColor = Color.Black,
            elevation = 15.dp,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.FillHeight,
                contentDescription = contentDescription,
            )
        }
    }
}

@Composable
fun AudioVisualTitle(title: String, size: Dp) {
    Text(
        color = Color.White,
        text = title,
        modifier = Modifier
            .padding(top = 5.dp)
            .size(size).border(1.dp, color = Color.Yellow),
        fontSize = 13.sp,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}
