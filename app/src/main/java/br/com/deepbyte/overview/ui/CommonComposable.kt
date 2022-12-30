package br.com.deepbyte.overview.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.ui.search.ClearSearchIcon
import br.com.deepbyte.overview.ui.search.SearchIcon
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
import br.com.deepbyte.overview.util.MediaItemClick
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ehsanmsz.mszprogressindicator.progressindicator.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun BasicTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(
                bottom = 5.dp,
                top = 10.dp,
                start = dimensionResource(R.dimen.screen_padding)
            ),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SimpleTitle(title: String) {
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

@Composable
fun TrackScreenView(screen: ScreenNav, tracker: IAnalyticsTracker) {
    DisposableEffect(Unit) {
        tracker.screenViewEvent(screen.name, screen.name)
        onDispose {}
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
            BallScaleRippleMultipleProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = AccentColor,
                animationDuration = 900,
            )
        }
    }
}

@Composable
fun ErrorScreen(refresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

@Preview
@Composable
fun PreviewErrorScreen() {
    ErrorScreen {}
}

@Composable
fun IntermediateScreensText(text: String) {
    Text(
        text = text,
        color = AccentColor,
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(bottom = 20.dp)
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
        onClick = { onClick.invoke() },
        contentPadding = PaddingValues(10.dp),
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AccentColor
        )
    ) {
        Icon(
            iconImageVector,
            contentDescription = iconDescription,
            modifier = Modifier.size(30.dp),
            tint = PrimaryBackground
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = buttonText,
            color = PrimaryBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun ToolbarButton(
    painter: ImageVector,
    @StringRes descriptionResource: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    background: Color = PrimaryBackground.copy(alpha = 0.5f),
    padding: PaddingValues = PaddingValues(dimensionResource(R.dimen.screen_padding)),
    onClick: () -> Unit,
) {
    Box(
        modifier
            .padding(padding)
            .clip(CircleShape)
            .background(background)
            .size(dimensionResource(R.dimen.toolbar_element_size))
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
        color = AccentColor,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.screen_padding),
            vertical = dimensionResource(R.dimen.default_padding)
        ),
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
                Modifier
                    .padding(
                        vertical = dimensionResource(R.dimen.default_padding)
                    ),
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.screen_padding)
                )
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
fun MediaList(
    listTitle: String,
    medias: List<Media>,
    onClickItem: MediaItemClick
) {
    if (medias.isNotEmpty()) {
        Column {
            BasicTitle(listTitle)
            LazyRow(
                Modifier
                    .padding(
                        vertical = dimensionResource(R.dimen.default_padding)
                    ),
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.screen_padding)
                )
            ) {
                items(medias) { media ->
                    MediaItem(media, imageWithBorder = true) {
                        onClickItem.invoke(media.apiId, media.getType())
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItem(mediaItem: Media, imageWithBorder: Boolean = false, onClick: () -> Unit) {
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
                then (
                    if (withBorder) Modifier.border(
                        dimensionResource(R.dimen.border_width),
                        SecondaryBackground,
                        RoundedCornerShape(dimensionResource(R.dimen.corner))
                    ) else Modifier
                    ),
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
    SimpleSubtitle1(text = stringResource(R.string.separator), display = display)
}

@Composable
fun PartingEmDash(display: Boolean = true) {
    SimpleSubtitle1(text = stringResource(R.string.em_dash), display = display)
}

@Composable
fun SimpleSubtitle1(text: String, display: Boolean = true, isBold: Boolean = true) {
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
fun SimpleSubtitle2(text: String, display: Boolean = true, isBold: Boolean = true) {
    if (text.isNotEmpty() && display) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.subtitle2,
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
            .height(dimensionResource(R.dimen.backdrop_height))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner))),
        contentScale = ContentScale.Crop,
        contentDescription = contentDescription
    )
}

@Composable
fun <T> UiStateResult(
    uiState: UiState<T>,
    onRefresh: () -> Unit,
    successContent: @Composable
    (T) -> Unit
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
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            SimpleTitle(stringResource(paragraphTitle))
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
            .clip(CircleShape),
        placeholder = painterResource(R.drawable.avatar),
        errorDefaultImage = painterResource(R.drawable.avatar)
    )
}

@Composable
fun DiscoverContent(
    showAds: Boolean,
    providerName: String,
    pagingItems: LazyPagingItems<MediaItem>,
    onRefresh: () -> Unit,
    onPopBackStack: () -> Unit,
    onToMediaDetails: MediaItemClick,
) {
    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
        is LoadState.NotLoading -> {
            Scaffold(
                modifier = Modifier
                    .background(PrimaryBackground)
                    .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
                topBar = { DiscoverToolBar(providerName, onPopBackStack) },
                bottomBar = {
                    AdsBanner(R.string.banner_sample_id, showAds)
                }
            ) { padding ->
                if (pagingItems.itemCount == 0) {
                    ErrorOnLoading()
                } else {
                    DiscoverBody(padding, pagingItems, onToMediaDetails)
                }
            }
        }
        else -> ErrorScreen(onRefresh)
    }
}

@Composable
fun DiscoverBody(
    padding: PaddingValues,
    pagingItems: LazyPagingItems<MediaItem>,
    onNavigateToMediaDetails: MediaItemClick,
) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(padding)
            .fillMaxSize()
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {
            items(pagingItems.itemCount) { index ->
                GridItemMedia(
                    mediaItem = pagingItems[index],
                    onClick = onNavigateToMediaDetails
                )
            }
        }
    }
}

@Composable
fun DiscoverToolBar(screenTitle: String, backButtonAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon,
            background = Color.White.copy(alpha = 0.1f),
            padding = PaddingValues(
                vertical = dimensionResource(R.dimen.screen_padding),
                horizontal = 2.dp
            )
        ) { backButtonAction.invoke() }
        ScreenTitle(text = screenTitle)
    }
}

// TODO: To refactor user Media class as parameter
@Composable
fun GridItemMedia(mediaItem: MediaItem?, onClick: MediaItemClick) {
    mediaItem?.apply {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .clickable { onClick(apiId, type) }
        ) {
            BasicImage(
                url = getPosterImage(),
                contentDescription = getLetter(),
                withBorder = true,
                modifier = Modifier
                    .size(width = 125.dp, height = 180.dp)
                    .padding(1.dp)
            )
            BasicText(
                text = getLetter(),
                style = MaterialTheme.typography.caption,
                isBold = true
            )
        }
    }
}

@Composable
fun GridItemMedia(media: Media, onClick: (Media) -> Unit) {
    media.apply {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .clickable { onClick(this) }
        ) {
            BasicImage(
                url = getPosterImage(),
                contentDescription = getLetter(),
                withBorder = true,
                modifier = Modifier
                    .size(width = 125.dp, height = 180.dp)
                    .padding(1.dp)
            )
            BasicText(
                text = getLetter(),
                style = MaterialTheme.typography.caption,
                isBold = true
            )
        }
    }
}

@Preview
@Composable
fun ErrorOnLoading() {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            IntermediateScreensText(stringResource(R.string.error_on_loading))
        }
    }
}

@Composable
fun OfflineSnackBar(isNotOnline: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = isNotOnline,
        modifier = modifier
    ) {
        Snackbar(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.app_offline_msg),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    // TODO: fix this app not launch composer on real device.
    Text("\n ")
}

@Composable
fun ToolbarTitle(title: String, modifier: Modifier = Modifier, textPadding: Dp = 0.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, PrimaryBackground)
                )
            )
    ) {
        ScreenTitle(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(textPadding)
        )
    }
}

@Composable
fun SearchField(onSearch: (query: String) -> Unit) {

    var query by rememberSaveable { mutableStateOf(value = String()) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = { value -> query = value },
        maxLines = 1,
        placeholder = { Text(text = stringResource(R.string.search_field_text), color = AccentColor) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = dimensionResource(R.dimen.screen_padding)),
        singleLine = true,
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(query)
            focusManager.clearFocus()
        }),
        keyboardOptions = KeyboardOptions
            .Default.copy(imeAction = ImeAction.Search, keyboardType = KeyboardType.Text),
        textStyle = MaterialTheme.typography.subtitle1.copy(color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Color.White,
            backgroundColor = SecondaryBackground,
            focusedBorderColor = SecondaryBackground,
            unfocusedBorderColor = SecondaryBackground,
        ),
        leadingIcon = { SearchIcon() },
        trailingIcon = { ClearSearchIcon(query) { query = "" } },
        shape = RoundedCornerShape(100.dp),
    )
}
