package io.github.leoallvez.take.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ehsanmsz.mszprogressindicator.progressindicator.SquareSpinProgressIndicator
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.theme.Background
import io.github.leoallvez.take.ui.theme.BlueTake
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun BasicTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(
                bottom = 5.dp,
                top = 10.dp
            ),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
    )
}

@SuppressLint("TrackScreenView")
@Composable
fun TrackScreenView(screen: Screen, logger: Logger) {
    DisposableEffect(Unit){
        logger.logOpenScreen(screen.name)
        onDispose { logger.logExitScreen(screen.name) }
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .background(Background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            IntermediateScreensText(text = stringResource(R.string.loading))
            SquareSpinProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = BlueTake,
                animationDuration = 800,
                animationDelay = 200,
            )
        }
    }
}

@Composable
fun ErrorOnLoading(refresh: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            IntermediateScreensText(text = stringResource(R.string.error_on_loading))
            StylizedButton(
                buttonText = stringResource(R.string.btn_try_again),
                iconDescription = stringResource(R.string.refresh_icon),
                iconImageVector = Icons.Filled.Refresh
            ) {
                refresh.invoke()
            }
        }
    }
}

@Composable
fun IntermediateScreensText(text: String) {
    Text(
        text = text,
        color = BlueTake,
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(bottom = 40.dp)
            .width(200.dp)
    )
}

@Composable
fun StylizedButton(
    buttonText: String,
    iconDescription: String,
    iconImageVector: ImageVector,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.border(1.dp, BlueTake),
        onClick = { onClick.invoke() },
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 12.dp,
        )
    ) {
        Icon(
            iconImageVector,
            contentDescription = iconDescription,
            modifier = Modifier.size(ButtonDefaults.IconSize),
            tint = BlueTake
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = buttonText, color = BlueTake)
    }
}

@Composable
fun ToolbarButton(
    painter: ImageVector,
    @StringRes descriptionResource: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    onClick: () -> Unit
) {
    Box(
        modifier
            .padding(dimensionResource(R.dimen.screen_padding))
            .clip(CircleShape)
            .background(Background.copy(alpha = 0.5f))
            .size(45.dp)
            .clickable { onClick.invoke() }
    ) {

        Icon(
            painter,
            contentDescription = stringResource(descriptionResource),
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size))
                .align(Alignment.Center),
            tint = iconTint
        )
    }
}

@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier, maxLines: Int = Int.MAX_VALUE) {
    Text(
        text = text,
        color = BlueTake,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun MediaItemList(
    listTitle:String,
    items: List<MediaItem>,
    mediaType: String? = null,
    onClickItem: MediaItemClick
) {
    if (items.isNotEmpty()) {
        Column {
            BasicTitle(listTitle)
            LazyRow (
                Modifier.padding(
                    vertical = dimensionResource(R.dimen.default_padding)
                ),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding))
            ) {
                items(items) { item ->
                    MediaItem(item) {
                        onClickItem.invoke(item.apiId, mediaType ?: item.type)
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItem(mediaItem: MediaItem, onClick: () -> Unit) {
    Column(Modifier.clickable { onClick.invoke() }) {
        BasicImage(
            url = mediaItem.getPoster(),
            contentDescription = mediaItem.getLetter(),
        )
        BasicText(
            text = mediaItem.getLetter(),
            style =  MaterialTheme.typography.caption,
            isBold = true,
        )
    }
}

@Composable
fun BasicImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    height: Dp = dimensionResource(R.dimen.image_height),
    contentScale: ContentScale = ContentScale.FillHeight,
    placeholder: Painter = painterResource(R.drawable.placeholder),
    errorDefaultImage: Painter = painterResource(R.drawable.placeholder)
) {
    if (url.isNotEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = url)
                .crossfade(true)
                .build(),
            modifier = modifier
                .background(Background)
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner))),
            contentScale = contentScale,
            placeholder = placeholder,
            contentDescription = contentDescription,
            error = errorDefaultImage,
        )
    }
}

@Composable
fun BasicText(
    text: String,
    style: TextStyle,
    color: Color = Color.White,
    isBold: Boolean = false
) {
    Text(
        color = color,
        text = text,
        modifier = Modifier
            .padding(top = 3.dp)
            .width(dimensionResource(R.dimen.person_profiler_width)),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        fontWeight = if(isBold) FontWeight.Bold else FontWeight.Normal,
        style = style,
    )
}

@Composable
fun SimpleSubtitle(text: String, display: Boolean = true) {
    if (text.isNotEmpty() && display) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun Backdrop(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = url)
            .crossfade(true)
            .build(),
        modifier = modifier
            .background(Background)
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner))),
        contentScale = ContentScale.FillHeight,
        contentDescription = contentDescription,
        placeholder = painterResource(R.drawable.img_gargantua),
        error = painterResource(R.drawable.img_gargantua)
    )
}
