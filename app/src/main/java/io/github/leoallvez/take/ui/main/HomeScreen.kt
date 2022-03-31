package io.github.leoallvez.take.ui.main

import androidx.annotation.StringRes
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
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import io.github.leoallvez.take.R

@Preview
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
            HorizontalList(
                categoryTitle = "Os mais populares",
                movies
            )
            HorizontalList(
                categoryTitle = "Grátis para assistir",
                movies
            )
            HorizontalList(
                categoryTitle = "Documentários",
                movies
            )
            HorizontalList(
                categoryTitle = "Aventura",
                movies
            )
            HorizontalList(
                categoryTitle = "Clássicos",
                movies
            )
        }
    }
}
//TODO: On and Off with remote config.
//TODO: Create composable file.
@Composable
fun AdsBanner(@StringRes bannerId: Int) {
    Box(modifier = Modifier.padding(10.dp)) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    adSize = AdSize.BANNER
                    adUnitId = context.getString(bannerId)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Composable
fun HorizontalList(
    categoryTitle: String,
    movies: List<String>
) {
    //TODO: Add Loading
    Text(
        text = categoryTitle,
        color = Color.White,
        modifier = Modifier.padding(5.dp, bottom = 10.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    LazyRow {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
    }
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
