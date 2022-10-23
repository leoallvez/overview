package io.github.leoallvez.take.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import io.github.leoallvez.take.ui.theme.BlueTake
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.ui.theme.SecondaryBackground
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
        fontWeight = FontWeight.Bold
    )
}

@SuppressLint("TrackScreenView")
@Composable
fun TrackScreenView(screen: ScreenNav, logger: Logger) {
    DisposableEffect(Unit) {
        logger.logOpenScreen(screen.name)
        onDispose { logger.logExitScreen(screen.name) }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            IntermediateScreensText(text = stringResource(R.string.loading))
            SquareSpinProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = BlueTake,
                animationDuration = 800,
                animationDelay = 200
            )
        }
    }
}

@Composable
fun ErrorScreen(refresh: () -> Unit) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            vertical = 12.dp
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
    background: Color = PrimaryBackground.copy(alpha = 0.5f),
    onClick: () -> Unit
) {
    Box(
        modifier
            .padding(dimensionResource(R.dimen.screen_padding))
            .clip(CircleShape)
            .background(background)
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
    listTitle: String,
    items: List<MediaItem>,
    mediaType: String? = null,
    onClickItem: MediaItemClick
) {
    if (items.isNotEmpty()) {
        Column {
            BasicTitle(listTitle)
            LazyRow(
                Modifier.padding(
                    vertical = dimensionResource(R.dimen.default_padding)
                ),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding))
            ) {
                items(items) { item ->
                    MediaItem(item, imageWithBorder = true) {
                        onClickItem.invoke(item.apiId, mediaType ?: item.type)
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItem(mediaItem: MediaItem, imageWithBorder: Boolean = false, onClick: () -> Unit) {
    Column(Modifier.clickable { onClick.invoke() }) {
        BasicImage(
            url = mediaItem.getPosterImage(),
            contentDescription = mediaItem.getLetter(),
            withBorder = imageWithBorder
        )
        BasicText(
            text = mediaItem.getLetter(),
            style = MaterialTheme.typography.caption,
            isBold = true
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
    errorDefaultImage: Painter = painterResource(R.drawable.placeholder),
    withBorder: Boolean = false
) {
    if (url.isNotEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = url)
                .crossfade(true)
                .build(),
            modifier = modifier
                .background(PrimaryBackground)
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner)))
                    then (if (withBorder) Modifier.border(
                            dimensionResource(R.dimen.border_width),
                            SecondaryBackground,
                            RoundedCornerShape(dimensionResource(R.dimen.corner))
                    ) else Modifier),
            contentScale = contentScale,
            placeholder = placeholder,
            contentDescription = contentDescription,
            error = errorDefaultImage
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
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        style = style
    )
}

@Composable
fun PartingPoint(display: Boolean = true) {
    SimpleSubtitle(text = stringResource(R.string.separator), display = display)
}

@Composable
fun PartingEmDash(display: Boolean = true) {
    SimpleSubtitle(text = stringResource(R.string.em_dash), display = display)
}

@Composable
fun SimpleSubtitle(text: String, display: Boolean = true, isBold: Boolean = true) {
    if (text.isNotEmpty() && display) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
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
            .background(SecondaryBackground)
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner))),
        contentScale = ContentScale.FillHeight,
        contentDescription = contentDescription
    )
}

@Composable
fun <T> UiStateResult(
    uiState: UiState<T>,
    onRefresh: () -> Unit,
    successContent: @Composable (T) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success -> successContent(uiState.data)
        else -> ErrorScreen { onRefresh() }
    }
}

@Composable
fun BasicParagraph(@StringRes paragraphTitle: Int, paragraph: String) {
    if (paragraph.isNotBlank()) {
        Column {
            BasicTitle(stringResource(paragraphTitle))
            BasicParagraph(paragraph)
        }
    }
}

@Composable
fun BasicParagraph(paragraph: String) {
    Text(
        text = paragraph,
        color = Color.White,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun PersonImageCircle(imageUrl: String, contentDescription: String, modifier: Modifier = Modifier) {
    BasicImage(
        url = imageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(dimensionResource(R.dimen.border_width), SecondaryBackground, CircleShape),
        placeholder = painterResource(R.drawable.avatar),
        errorDefaultImage = painterResource(R.drawable.avatar)
    )
}
