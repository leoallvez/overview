package br.dev.singular.overview.ui

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagBottomNavigation
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.ui.navigation.BottomNavigation
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.DarkGray
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.MediaItemClick
import br.dev.singular.overview.util.getStringByName
import br.dev.singular.overview.util.onClick
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ehsanmsz.mszprogressindicator.progressindicator.BallScaleRippleMultipleProgressIndicator

@Composable
fun GenreEntity.nameTranslation(): String {
    val translationName = getGenreTranslation.invoke(apiId)
    return if (translationName.isNullOrEmpty()) name else translationName
}

private val getGenreTranslation = @Composable { apiId: Long ->
    val current = LocalContext.current
    current.getStringByName(resource = "genre_$apiId")
}

@Composable
fun BasicTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(
                bottom = 5.dp,
                top = 10.dp,
                start = dimensionResource(R.dimen.spacing_s)
            ),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SimpleTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color.White,
        modifier = modifier
            .padding(
                bottom = 5.dp,
                top = 10.dp
            ),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TagScreenView(
    tagPath: String,
    status: String = ""
) {
    DisposableEffect(Unit) {
        TagManager.logScreenView(tagPath, status)
        onDispose { /* no-op */ }
    }
}

@Composable
fun LoadingScreen(tagPath: String) {

    TagScreenView(tagPath, TagStatus.LOADING)
    Column(
        modifier = Modifier.background(PrimaryBackground).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IntermediateScreensText(text = stringResource(R.string.loading))
        BallScaleRippleMultipleProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = AccentColor,
            animationDuration = 900
        )
    }
}

@Composable
fun ErrorScreen(
    tagPath: String,
    refresh: () -> Unit
) {
    TagScreenView(tagPath, TagStatus.ERROR)
    Column(
        modifier = Modifier.fillMaxSize().background(PrimaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IntermediateScreensText(
            text = stringResource(R.string.error_on_loading),
            color = AlertColor
        )
        StylizedButton(
            buttonText = stringResource(R.string.btn_try_again),
            iconDescription = stringResource(R.string.refresh_icon),
            iconImageVector = Icons.Filled.Refresh
        ) {
            refresh.invoke()
        }
    }
}

@Composable
fun NothingFoundScreen(
    tagPath: String,
    hasFilters: Boolean = false
) {
    TagScreenView(tagPath, TagStatus.NOTHING_FOUND)

    Column(
        modifier = Modifier.fillMaxSize().background(PrimaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IntermediateScreensText(
            text = stringResource(R.string.not_found),
            color = AlertColor
        )
        if (hasFilters) {
            Text(
                text = stringResource(id = R.string.check_filters),
                color = AccentColor,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun IntermediateScreensText(text: String, color: Color = AccentColor) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.titleLarge,
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
            containerColor = AccentColor
        )
    ) {
        Icon(
            iconImageVector,
            contentDescription = iconDescription,
            modifier = Modifier.size(35.dp),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ButtonWithIcon(
    painter: ImageVector,
    @StringRes descriptionResource: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    withBorder: Boolean = true,
    background: Color = PrimaryBackground.copy(alpha = 0.5f),
    padding: PaddingValues = PaddingValues(dimensionResource(R.dimen.spacing_s)),
    onLongClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Box(
        modifier
            .padding(padding)
            .clip(CircleShape)
            .background(background)
            .size(35.dp)
            .combinedClickable(
                onClick = onClick::invoke,
                onLongClick = onLongClick::invoke
            )
            .then(if (withBorder) Modifier.border(1.dp, DarkGray, CircleShape) else Modifier)
    ) {
        Icon(
            painter,
            contentDescription = stringResource(descriptionResource),
            modifier = Modifier
                .size(dimensionResource(R.dimen.spacing_xl))
                .align(Alignment.Center),
            tint = iconTint
        )
    }
}

@Deprecated(
    message = """
        br.dev.singular.overview.ui.ScreenTitle is deprecated. 
        Use br.dev.singular.overview.presentation.ui.text.SectionTitle
        instead for better flexibility and design consistency.""",
    level = DeprecationLevel.WARNING
)
@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier, maxLines: Int = Int.MAX_VALUE) {
    Text(
        text = text,
        color = AccentColor,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.spacing_xs),
            vertical = dimensionResource(R.dimen.spacing_xs)
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Deprecated(
    message = """
        br.dev.singular.overview.ui.MediaList is deprecated. 
        Use br.dev.singular.overview.presentation.ui.media.HorizontalMediaList 
        instead for better flexibility and design consistency.""",
    level = DeprecationLevel.WARNING
)
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
                        vertical = dimensionResource(R.dimen.spacing_s)
                    ),
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.spacing_xs)
                )
            ) {
                items(medias.size) { index ->
                    val media = medias[index]
                    MediaItem(media, imageWithBorder = true) {
                        onClickItem.invoke(media.apiId, media.getType())
                    }
                }
            }
        }
    }
}

@Deprecated(
    message = """
        br.dev.singular.overview.ui.MediaItem is deprecated. 
        Use br.dev.singular.overview.presentation.ui.media.MediaItem
        instead for better flexibility and design consistency.""",
    level = DeprecationLevel.WARNING
)
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
            style = MaterialTheme.typography.bodySmall,
            isBold = true
        )
    }
}

@Deprecated(
    message = """
        br.dev.singular.overview.ui.image.BasicImage is deprecated. 
        Use br.dev.singular.overview.presentation.ui.image.BasicImage
        instead for better flexibility and design consistency.""",
    level = DeprecationLevel.WARNING
)
@Composable
fun BasicImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    height: Dp = dimensionResource(R.dimen.poster_height),
    contentScale: ContentScale = ContentScale.FillHeight,
    placeholder: Painter = painterResource(R.drawable.placeholder),
    errorDefaultImage: Painter = painterResource(R.drawable.placeholder),
    corner: Dp = dimensionResource(R.dimen.corner_width),
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
                .clip(RoundedCornerShape(corner))
                .then(Modifier.border(withBorder)),
            contentScale = contentScale,
            placeholder = placeholder,
            contentDescription = contentDescription,
            error = errorDefaultImage
        )
    }
}

@Deprecated(
    message = """
        br.dev.singular.overview.ui.border is deprecated. 
        Use br.dev.singular.overview.presentation.ui.utils.border
        instead for better flexibility and design consistency.""",
    level = DeprecationLevel.WARNING
)
@Composable
fun Modifier.border(
    withBorder: Boolean,
    color: Color = DarkGray,
    width: Dp = dimensionResource(R.dimen.border_width)
): Modifier = composed {
    if (withBorder) {
        border(width, color, RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
    } else {
        this
    }
}

@Deprecated(
    message = """
        br.dev.singular.overview.ui.border is deprecated. 
        Use br.dev.singular.overview.presentation.ui.utils.border
        instead for better flexibility and design consistency.""",
    level = DeprecationLevel.WARNING
)
@Composable
fun BasicText(text: String, style: TextStyle, color: Color = Color.White, isBold: Boolean = false) {
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SimpleSubtitle2(
    text: String,
    display: Boolean = true,
    isBold: Boolean = true,
    color: Color = Color.White
) {
    if (text.isNotEmpty() && display) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.titleSmall,
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
            .height(dimensionResource(R.dimen.poster_height))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_width))),
        contentScale = ContentScale.Crop,
        contentDescription = contentDescription
    )
}

@Composable
fun <T> UiStateResult(
    uiState: UiState<T>,
    tagPath: String,
    onRefresh: () -> Unit,
    successContent: @Composable (T) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> LoadingScreen(tagPath)
        is UiState.Success -> {
            TagScreenView(tagPath, TagStatus.SUCCESS)
            successContent(uiState.data)
        }
        else -> ErrorScreen(tagPath) { onRefresh() }
    }
}

@Composable
fun BasicParagraph(@StringRes paragraphTitle: Int, paragraph: String) {
    if (paragraph.isNotBlank()) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.spacing_s)
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
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun PersonImageCircle(person: Person, modifier: Modifier = Modifier) {
    BasicImage(
        url = person.getProfileImage(),
        contentDescription = person.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape),
        placeholder = painterResource(R.drawable.avatar),
        errorDefaultImage = painterResource(R.drawable.avatar)
    )
}

@Composable
fun MediaEntityPagingVerticalGrid(
    padding: PaddingValues = PaddingValues(),
    tagPath: String,
    items: LazyPagingItems<MediaEntity>,
    onClick: MediaItemClick
) {
    TagScreenView(tagPath, TagStatus.SUCCESS)
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(top = padding.calculateTopPadding())
            .fillMaxSize()
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
            items(items.itemCount) { index ->
                GridItemMediaEntity(
                    media = items[index],
                    onClick = {
                        TagMediaManager.logClick(tagPath, it.apiId)
                        onClick.invoke(it.apiId, it.type)
                    }
                )
            }
        }
    }
}

@Composable
fun GridItemMediaEntity(media: MediaEntity?, onClick: (MediaEntity) -> Unit) {
    media?.apply {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .clickable { onClick(media) }
        ) {
            BasicImage(
                url = getPosterImage(),
                contentDescription = letter,
                withBorder = true,
                modifier = Modifier.padding(1.dp)
            )
            BasicText(
                text = letter,
                style = MaterialTheme.typography.bodySmall,
                isBold = true
            )
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
}

@Composable
fun ToolbarTitle(
    title: String,
    modifier: Modifier = Modifier,
    textPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
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
fun StreamingIcon(
    modifier: Modifier = Modifier,
    streaming: StreamingEntity?,
    size: Dp = 48.dp,
    withBorder: Boolean = true,
    corner: Dp = dimensionResource(id = R.dimen.corner_width),
    onClick: (() -> Unit)? = null
) {
    streaming?.let {
        BasicImage(
            corner = corner,
            url = streaming.getLogoImage(),
            contentDescription = streaming.name,
            withBorder = withBorder,
            modifier = modifier
                .size(size)
                .onClick(action = onClick)
        )
    }
}

@Composable
fun MediaTypeSelector(selectedKey: String, onClick: (MediaType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.spacing_xs))
    ) {
        val options = MediaType.getAllOrdered()
        options.forEach { mediaType ->
            MediaTypeFilterButton(mediaType, selectedKey) {
                onClick.invoke(mediaType)
            }
        }
    }
}

@Composable
fun MediaTypeFilterButton(
    mediaType: MediaType,
    selectedKey: String,
    onClick: () -> Unit
) {
    val isActivated = selectedKey == mediaType.key
    val focusManager = LocalFocusManager.current

    FilterButton(
        onClick = {
            onClick.invoke()
            focusManager.clearFocus()
        },
        isActivated = isActivated,
        backgroundColor = SecondaryBackground,
        buttonText = stringResource(mediaType.labelRes)
    )
}

@Composable
fun FilterButton(
    buttonText: String?,
    isActivated: Boolean = false,
    colorActivated: Color = AccentColor,
    backgroundColor: Color = PrimaryBackground,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = dimensionResource(R.dimen.spacing_xs)
    ),
    complement: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    val color = if (isActivated) colorActivated else Gray
    OutlinedButton(
        onClick = { onClick.invoke() },
        shape = RoundedCornerShape(percent = 100),
        contentPadding = contentPadding,
        modifier = Modifier
            .height(30.dp)
            .padding(PaddingValues(end = dimensionResource(R.dimen.spacing_s))),
        border = BorderStroke(dimensionResource(R.dimen.border_width), color),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        buttonText?.let {
            Text(
                text = it,
                color = color,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isActivated) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(5.dp)
            )
        }
        complement()
    }
}

@Composable
fun DefaultVerticalSpace() {
    Spacer(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(vertical = dimensionResource(R.dimen.spacing_xs))
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, adBannerIsVisible: Boolean) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    if (currentRoute != ScreenNav.Splash.route && currentRoute != ScreenNav.YouTubePlayer.route) {
        Column {
            AdsBanner(R.string.bottom_navigation, isVisible = adBannerIsVisible)
            BottomNavigation {
                val items = BottomNavigation.items
                items.forEach { item ->
                    val isSelected = currentRoute == item.nav.route
                    val color = if (isSelected) AccentColor else Gray
                    BottomNavigationItem(
                        modifier = Modifier.background(PrimaryBackground).padding(bottom = 5.dp),
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = stringResource(id = item.title),
                                tint = color
                            )
                        },
                        label = { Text(stringResource(item.title), color = color) },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute == item.nav.route) return@BottomNavigationItem
                            TagManager.logClick(TagBottomNavigation.PATH, item.tagDetail)
                            navController.navigate(item.nav.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}
