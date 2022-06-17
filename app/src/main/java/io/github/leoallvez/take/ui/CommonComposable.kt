package io.github.leoallvez.take.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.MediaItem
import timber.log.Timber

@Composable
fun ListTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(
                start = 5.dp,
                bottom = 5.dp,
                top = 15.dp
            ),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun MediaCard(
    media: MediaItem,
    titleHeight: Dp = 65.dp,
    imageHeight: Dp = 200.dp,
    columnWith: Dp = 140.dp,
    titleFontSize: TextUnit = 12.sp,
    titleMaxLines: Int = 3
) {
    Column(
        modifier = Modifier
            .size(
                width = columnWith,
                height = imageHeight + titleHeight
            ).padding(5.dp)
            .clickable {
                Timber
                    .tag("click_audio")
                    .i("click on: ${media.getItemTitle()}")
            }
    ) {
        MediaImage(
            imageUrl = media.getItemPoster(),
            contentDescription = media.getItemTitle(),
            modifier = Modifier
                .size(
                    width = columnWith,
                    height = imageHeight
                )
        )
        MediaTitle(
            title = media.getItemTitle(),
            width = columnWith,
            height = titleHeight,
            fontSize = titleFontSize,
            maxLines = titleMaxLines,
        )
    }
}

@Composable
fun MediaImage(
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
fun MediaTitle(
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
            .size(width = width, height = height),
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}

@SuppressLint("ComposableNaming")
@Composable
fun TrackScreenView(screenName: String, logger: Logger) {
    DisposableEffect(Unit){
        logger.logScreenView(screenName)
        onDispose { /**Log.d("SCREENTRACKING", "screen exit : $name")*/ }
    }
}
