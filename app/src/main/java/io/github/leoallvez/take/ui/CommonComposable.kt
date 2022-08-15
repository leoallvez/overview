package io.github.leoallvez.take.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ehsanmsz.mszprogressindicator.progressindicator.SquareSpinProgressIndicator
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.theme.BlueTake
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.FabPosition

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
    onClick: (mediaId: Long) -> Unit,
) {
    val imageHeight: Dp = 200.dp
    val titleHeight: Dp = 65.dp
    val cardWith: Dp = 140.dp
    Column(
        modifier = Modifier
            .size(
                width = cardWith,
                height = imageHeight + titleHeight
            )
            .padding(all = 5.dp)
            .clickable { onClick.invoke(media.apiId) }
    ) {
        MediaImage(
            media = media,
            modifier = Modifier
                .size(
                    width = cardWith,
                    height = imageHeight
                )
        )
        MediaTitle(
            title = media.getItemTitle(),
            width = cardWith,
            height = titleHeight,
            fontSize = 12.sp,
            maxLines = 3,
        )
    }
}

@Composable
fun MediaImage(
    media: MediaItem,
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
                    .data(data = media.getItemPoster())
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.FillHeight,
                contentDescription = media.getItemTitle(),
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
            .background(Color.Black)
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

@Preview
@Composable
fun LoadingIndicatorPreview() {
    LoadingIndicator()
}

@Composable
fun ErrorOnLoading(refresh: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.Black)
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
            Icons.Filled.Refresh,
            contentDescription = stringResource(R.string.refresh_icon),
            modifier = Modifier.size(ButtonDefaults.IconSize),
            tint = BlueTake
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = stringResource(R.string.btn_try_again), color = BlueTake)
    }
}

@Composable
@Preview
fun ErrorOnLoadingPreview() {
    ErrorOnLoading {}
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
            .background(Color.Black.copy(alpha = 0.5f))
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
fun CardImage(
    data: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = data)
            .crossfade(true)
            .build(),
        modifier = modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(235.dp),
        contentScale = ContentScale.FillHeight,
        contentDescription = contentDescription,
    )
}

@Composable
fun CollapsingToolbarScope.BackdropImage(data: String?, contentDescription: String?) {
    CardImage(data, contentDescription, modifier = Modifier
        .parallax(ratio = 0.2f)
        .pin()
    )
}

@Composable
fun BackdropTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 7.dp, top = 5.dp, bottom = 5.dp),
        color = BlueTake,
        style = MaterialTheme.typography.h6
    )
}

@Composable
fun ScreenTitle(text: String) {
    Text(
        text = text,
        color = BlueTake,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun ScreenSubtitle(text: String) {
    if(text.isNotEmpty()) {
        Text(
            text = text,
            color = Color.LightGray,
            style = MaterialTheme.typography.subtitle2,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(vertical = 10.dp),
        )
    }
}

@Composable
fun BodyText(text: String) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(vertical = 5.dp),
        textAlign = TextAlign.Justify
    )
}
