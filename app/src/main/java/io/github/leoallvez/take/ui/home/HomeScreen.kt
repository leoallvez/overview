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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.leoallvez.take.R
import io.github.leoallvez.take.ui.AdsBanner
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import io.github.leoallvez.take.data.model.Audiovisual
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
                isVisible = showAd.value
            )
            MovieSuggestionVerticalList(
                moviesSuggestions = suggestions
            )
        }
    }
}

@Composable
fun MovieSuggestionVerticalList(
    moviesSuggestions: List<SuggestionResult>
) {
    val context = LocalContext.current
    LazyColumn {
        items(moviesSuggestions) {
            AudiovisualHorizontalList(
                title = context.getStringByName(it.titleResourceId),
                contents = it.audiovisuals
            )
        }
    }
}

@Composable
fun AudiovisualHorizontalList(
    title: String,
    contents: List<Audiovisual>
) {
    ListTitle(title)
    LazyRow {
        items(contents) { content ->
            MovieCard(content.getContentTitle())
        }
    }
}

@Composable
fun ListTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier.padding(5.dp, bottom = 10.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun MovieCard(movie: String) {
    Card(
        shape = RoundedCornerShape(5.dp),
        contentColor = Color.Black,
        elevation = 12.dp,
        modifier = Modifier
            .size(width = 125.dp, height = 175.dp)
            .padding(5.dp, bottom = 30.dp)
    ) {
        Text(
            text = movie,
            modifier = Modifier
                .padding(10.dp),
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen(viewModel = null)
}
