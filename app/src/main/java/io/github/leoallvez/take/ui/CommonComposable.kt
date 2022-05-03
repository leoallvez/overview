package io.github.leoallvez.take.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.AudioVisual

@Composable
fun ListTitle(title: String) {
    Text(
        text = title,
        color = Color(0xFFFFB400),
        modifier = Modifier
            .padding(
                start = 5.dp,
                bottom = 5.dp,
                top = 15.dp
            ),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun HorizontalAudioVisualCard(audioVisual: AudioVisual) {
    AudioVisualCard(
        audioVisual = audioVisual,
        titleHeight = 65.dp,
        imageHeight = 200.dp,
        columnWith = 140.dp,
        titleFontSize = 13.sp,
        titleMaxLines = 3
    )
}

@Composable
fun AudioVisualCard(
    audioVisual: AudioVisual,
    titleHeight: Dp,
    imageHeight: Dp,
    columnWith: Dp,
    titleFontSize: TextUnit,
    titleMaxLines: Int
) {
    Column(
        modifier = Modifier
            .size(
                width = columnWith,
                height = imageHeight + titleHeight
            ).padding(5.dp)
            .clickable {
                Log.i("click_audio", "click on: ${audioVisual.getContentTitle()}")
            }
    ) {
        AudioVisualImage(
            imageUrl = audioVisual.getImageUrl(),
            contentDescription = audioVisual.getContentTitle(),
            modifier = Modifier
                .size(
                    width = columnWith,
                    height = imageHeight
                )//.border(1.dp, color = Color.DarkGray),
        )
        AudioVisualTitle(
            title = audioVisual.getContentTitle(),
            width = columnWith,
            height = titleHeight,
            fontSize = titleFontSize,
            maxLines = titleMaxLines,
        )
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
fun AudioVisualTitle(
    title: String,
    width: Dp,
    height: Dp,
    fontSize: TextUnit,
    maxLines: Int
) {
    Text(
        color = Color.White,
        text = title,
        modifier = Modifier
            .padding(top = 5.dp)
            .size(width = width, height = height), //.border(1.dp, color = Color.Yellow),
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}
