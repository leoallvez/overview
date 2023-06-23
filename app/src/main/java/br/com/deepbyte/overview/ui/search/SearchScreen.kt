package br.com.deepbyte.overview.ui.search

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.ALL
import br.com.deepbyte.overview.ui.AdsBanner
import br.com.deepbyte.overview.ui.GridItemMedia
import br.com.deepbyte.overview.ui.IntermediateScreensText
import br.com.deepbyte.overview.ui.LoadingScreen
import br.com.deepbyte.overview.ui.MediaTypeSelector
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.SearchField
import br.com.deepbyte.overview.ui.SearchState
import br.com.deepbyte.overview.ui.ToolbarButton
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.AlertColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.util.MediaItemClick

@Composable
fun SearchScreen(
    events: BasicsMediaEvents,
    viewModel: SearchViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Search, viewModel.analyticsTracker)

    Scaffold(
        backgroundColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            SearchToolBar(events::onNavigateToHome) { query ->
                viewModel.search(query)
            }
        },
        bottomBar = {
            AdsBanner(R.string.search_banner, viewModel.showAds)
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            when (val uiState = viewModel.uiState.collectAsState().value) {
                is SearchState.NotStated -> SearchIsNotStated()
                is SearchState.Loading -> LoadingScreen()
                is SearchState.Success -> {
                    SearchSuccess(uiState.data, events::onNavigateToMediaDetails)
                }
                is SearchState.Empty -> SearchIsEmpty()
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
        SearchField(placeholder = "placeholder", onSearch)
    }
}

@Composable
fun SearchIsNotStated() {
    CenteredTextString(R.string.search_not_started)
}

@Composable
fun SearchIsEmpty() {
    CenteredTextString(R.string.search_result_empty, AlertColor)
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
fun SearchSuccess(
    results: Map<String, List<Media>>,
    onNavigateToMediaDetails: MediaItemClick
) {
    var selected by remember { mutableStateOf(ALL.key) }

    Column {
        MediaTypeSelector(selected) { newSelected ->
            selected = newSelected.key
        }
        Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)))
        MediaGrind(medias = results[selected], onNavigateToMediaDetails)
    }
}

@Composable
fun MediaGrind(
    medias: List<Media>?,
    onNavigateToMediaDetails: MediaItemClick
) {
    if (medias.isNullOrEmpty()) {
        SearchIsEmpty()
    } else {
        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {
                items(medias.size) { index ->
                    GridItemMedia(
                        media = medias[index],
                        onClick = { media ->
                            onNavigateToMediaDetails.invoke(media.apiId, media.getType())
                        }
                    )
                }
            }
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
