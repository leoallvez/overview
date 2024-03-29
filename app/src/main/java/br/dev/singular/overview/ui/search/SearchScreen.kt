package br.dev.singular.overview.ui.search

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.R
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.ButtonWithIcon
import br.dev.singular.overview.ui.DefaultVerticalSpace
import br.dev.singular.overview.ui.IntermediateScreensText
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaEntityPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeSelector
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SearchField
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.PrimaryBackground

@Composable
fun SearchScreen(
    navigate: BasicNavigate,
    viewModel: SearchViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Search, viewModel.analyticsTracker)

    val filters = viewModel.filters.collectAsState().value
    val items = viewModel.medias.collectAsLazyPagingItems()

    Scaffold(
        contentColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            SearchToolBar(filters.query, navigate::popBackStack) { newQuery ->
                viewModel.updateFilter(filters.copy(query = newQuery))
            }
        },
        bottomBar = {
            AdsBanner(R.string.search_banner, viewModel.showAds)
        }
    ) { padding ->
        Column(
            modifier = Modifier.background(PrimaryBackground).padding(padding)
        ) {
            if (items.itemCount > 0 || filters.query.isNotEmpty()) {
                MediaTypeSelector(filters.mediaType.key) { newType ->
                    viewModel.updateFilter(filters.copy(mediaType = newType))
                }
            }
            DefaultVerticalSpace()
            Box {
                when (items.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.NotLoading -> {
                        MediaEntityPagingVerticalGrid(items = items, onClick = navigate::toMediaDetails)
                    }
                    else -> {
                        if (items.itemCount == 0 && filters.query.isNotEmpty()) {
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
    query: String,
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
        ButtonWithIcon(
            painter = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            descriptionResource = R.string.backstack_icon,
            background = Color.White.copy(alpha = 0.1f),
            padding = PaddingValues(
                vertical = dimensionResource(R.dimen.screen_padding),
                horizontal = 2.dp
            ),
            onClick = backButtonAction::invoke,
            onLongClick = backButtonAction::invoke
        )
        SearchField(
            onSearch = onSearch,
            autoOpenKeyboard = query.isEmpty(),
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
