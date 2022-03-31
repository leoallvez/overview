package io.github.leoallvez.take.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun HomeScreen() {
    val movies: List<String> = (0..99).map { "movie $it" }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            AdsBanner(bannerId = R.string.banner_sample_id)
            HorizontalList(title = "Os mais populares", movies)
            HorizontalList(title = "Grátis para assistir", movies)
            HorizontalList(title = "Documentários", movies)
            HorizontalList(title = "Aventura", movies)
            HorizontalList(title = "Clássicos", movies)
        }
    }
}

@Composable
fun HorizontalList(
    title: String,
    movies: List<String>
) {
    //TODO: Add Loading;
    ListTitle(title)
    LazyRow {
        items(movies) { movie ->
            MovieCard(movie)
        }
    }
}

@Composable
fun ListTitle(title: String) {
    Text(
        //TODO: Change title param to @StringRes;
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(5.dp, bottom = 10.dp),
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
    HomeScreen()
}
