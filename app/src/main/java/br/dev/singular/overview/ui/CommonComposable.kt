package br.dev.singular.overview.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.screens.common.ErrorScreen
import br.dev.singular.overview.presentation.ui.screens.common.LoadingScreen
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.ui.navigation.BottomNavigation
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.DarkGray
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.getStringByName
import br.dev.singular.overview.util.onClick
import coil.compose.AsyncImage
import coil.request.ImageRequest

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
    val paddingTop = dimensionResource(R.dimen.spacing_17x)
    when (uiState) {
        is UiState.Loading -> LoadingScreen(
            tagPath,
            modifier = Modifier.padding(top = paddingTop)
        )
        is UiState.Success -> {
            TrackScreenView(tagPath, TagStatus.SUCCESS)
            successContent(uiState.data)
        }
        else -> ErrorScreen(
            tagPath,
            modifier = Modifier.padding(top = paddingTop),
            onRefresh = onRefresh
        )
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
        placeholder = R.drawable.avatar,
        errorDefaultImage = R.drawable.avatar
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
                            UiIcon(
                                source = UiIconSource.vector(item.icon),
                                color =  color,
                                modifier = Modifier
                                    .size(dimensionResource( R.dimen.spacing_7x))
                                    .padding(bottom = dimensionResource( R.dimen.spacing_1x)),
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
