package br.dev.singular.overview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagBottomNavigation
import br.dev.singular.overview.presentation.ui.components.UiAdsBanner
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.ui.navigation.BottomNavigation
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.getStringByName
import br.dev.singular.overview.util.onClick

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
            errorDefaultImage = R.drawable.error_streaming_logo_placeholder,
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
            UiAdsBanner(R.string.bottom_navigation, isVisible = adBannerIsVisible)
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
