package br.dev.singular.overview.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagBottomNavigation
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.ui.navigation.BottomNavigation
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.DarkGray
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
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
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxSize(),
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
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
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
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
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
    onLongClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Box(
        modifier
            .clip(CircleShape)
            .background(background)
            .size(dimensionResource(id = R.dimen.spacing_8x))
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
                .size(dimensionResource(R.dimen.spacing_6x))
                .align(Alignment.Center),
            tint = iconTint
        )
    }
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
            .aspectRatio(16f / 9f)
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
fun BasicParagraph(@StringRes title: Int, paragraph: String) {
    if (paragraph.isNotBlank()) {
        Column {
            UiTitle(stringResource(title))
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
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_1x)),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun PersonImageCircle(person: Person, modifier: Modifier = Modifier) {
    UiImage(
        url = person.getProfileImage(),
        contentDescription = person.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(
                dimensionResource(R.dimen.border_width),
                DarkGray,
                CircleShape
            ),
        placeholder = painterResource(R.drawable.avatar),
        errorDefaultImage = painterResource(R.drawable.avatar)
    )
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
            .height(dimensionResource(R.dimen.spacing_15x))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, PrimaryBackground)
                )
            )
    ) {
        UiTitle(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(textPadding),
            color = AccentColor
        )
    }
}

@Composable
fun MainToolbarTitle(
    title: String,
    modifier: Modifier = Modifier,
    textPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x))
    ) {
        UiTitle(
            text = title,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(textPadding),
            color = AccentColor
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
        UiImage(
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
fun DefaultVerticalSpace() {
    Spacer(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(vertical = dimensionResource(R.dimen.spacing_1x))
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, adBannerIsVisible: Boolean) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val hiddenRoutes = setOf(ScreenNav.Splash.route, ScreenNav.YouTubePlayer.route)
    val height = dimensionResource(R.dimen.spacing_14x)
    if (currentRoute !in hiddenRoutes) {
        Column {
            AdsBanner(R.string.bottom_navigation, isVisible = adBannerIsVisible)
            BottomNavigation {
                val items = BottomNavigation.items
                items.forEach { item ->
                    val isSelected = currentRoute == item.nav.route
                    val color = if (isSelected) AccentColor else Gray
                    BottomNavigationItem(
                        modifier = Modifier.height(height).background(PrimaryBackground),
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
    } else if (currentRoute == ScreenNav.Splash.route) {
        Spacer(Modifier.height(height))
    }
}
