package dev.com.singular.overview.ui.search

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import dev.com.singular.overview.ui.AdsBanner
import dev.com.singular.overview.ui.IntermediateScreensText
import dev.com.singular.overview.ui.LoadingScreen
import dev.com.singular.overview.ui.MediaPagingVerticalGrid
import dev.com.singular.overview.ui.MediaTypeSelector
import dev.com.singular.overview.ui.NotFoundContentScreen
import dev.com.singular.overview.ui.ScreenNav
import dev.com.singular.overview.ui.SearchField
import dev.com.singular.overview.ui.ToolbarButton
import dev.com.singular.overview.ui.TrackScreenView
import dev.com.singular.overview.ui.navigation.wrappers.BasicNavigate
import dev.com.singular.overview.ui.theme.AccentColor
import dev.com.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.R

@Composable
fun SearchScreen(
    navigate: BasicNavigate,
    viewModel: SearchViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Search, viewModel.analyticsTracker)

    val loadData = {
        viewModel.searchPaging()
    }

    val filters = viewModel.searchFilters.collectAsState().value
    var mediaItems by remember { mutableStateOf(value = loadData()) }
    val setMediaItems = {
        mediaItems = loadData()
        viewModel.start()
    }

    Scaffold(
        backgroundColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            SearchToolBar(navigate::popBackStack) { query ->
                filters.query = query
                setMediaItems()
            }
        },
        bottomBar = {
            AdsBanner(R.string.search_banner, viewModel.showAds)
        }
    ) { padding ->
        Column {
            if (viewModel.started) {
                MediaTypeSelector(filters.mediaType.key) { mediaType ->
                    filters.mediaType = mediaType
                    setMediaItems()
                }
            }
            Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)))
            Box {
                val items = mediaItems.collectAsLazyPagingItems()
                when (items.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.NotLoading -> {
                        MediaPagingVerticalGrid(padding, items, navigate::toMediaDetails)
                    } else -> {
                        if (viewModel.started) {
                            NotFoundContentScreen()
                        } else {
                            SearchIsNotStated()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchToolBar(
    backButtonAction: () -> Unit,
    onSearch: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.screen_padding)),
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
        SearchField(
            onSearch = onSearch,
            placeholder = stringResource(R.string.search_in_all_places)
        )
    }
}

@Composable
fun SearchIsNotStated() {
    CenteredTextString(R.string.search_not_started)
}

@Composable
fun CenteredTextString(@StringRes textRes: Int, color: Color = AccentColor) {
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
            IntermediateScreensText(stringResource(textRes), color)
        }
    }
}

@Composable
fun ClearSearchIcon(query: String, onClick: () -> Unit) {
    if (query.isNotEmpty()) {
        IconButton(onClick = onClick) {
            Icon(
                tint = AccentColor,
                imageVector = Icons.Rounded.Clear,
                contentDescription = stringResource(R.string.search_icon)
            )
        }
    }
}

@Composable
fun SearchIcon(modifier: Modifier = Modifier) {
    Icon(
        tint = AccentColor,
        modifier = modifier,
        imageVector = Icons.Rounded.Search,
        contentDescription = stringResource(R.string.search_icon)
    )
}
