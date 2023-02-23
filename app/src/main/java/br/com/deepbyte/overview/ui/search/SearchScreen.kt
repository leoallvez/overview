package br.com.deepbyte.overview.ui.search

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.MediaType.*
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
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

        SearchField(onSearch)
    }
}

@Composable
fun SearchIsNotStated() {
    CenteredTextString(R.string.search_not_started)
}

@Composable
fun SearchIsEmpty() {
    CenteredTextString(R.string.search_result_empty)
}

@Composable
fun CenteredTextString(@StringRes textRes: Int) {
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
            IntermediateScreensText(stringResource(textRes))
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
        MediaSelector(selected) { newSelected ->
            selected = newSelected
        }
        MediaGrind(medias = results[selected], onNavigateToMediaDetails)
    }
}

@Composable
fun MediaSelector(selector: String, onClick: (String) -> Unit) {
    val options = listOf(ALL, MOVIE, TV_SHOW)
    Row(
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.default_padding))
    ) {
        options.forEach { option ->
            MediaButton(option.labelRes, selector, option.key, onClick)
        }
    }
    Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)))
}

@Composable
fun MediaButton(
    @StringRes labelResId: Int,
    selectedKey: String,
    mediaKey: String,
    onClick: (String) -> Unit
) {
    val isActivated = selectedKey == mediaKey
    val color = if (isActivated) AccentColor else Gray
    val focusManager = LocalFocusManager.current

    OutlinedButton(
        onClick = {
            onClick.invoke(mediaKey)
            focusManager.clearFocus()
        },
        shape = RoundedCornerShape(percent = 100),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.default_padding)
        ),
        modifier = Modifier
            .height(35.dp)
            .padding(end = dimensionResource(R.dimen.screen_padding)),
        border = BorderStroke(dimensionResource(R.dimen.border_width), color),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isActivated) PrimaryBackground else SecondaryBackground
        )
    ) {
        Text(
            text = stringResource(labelResId),
            color = color,
            style = MaterialTheme.typography.caption,
            fontWeight = if (isActivated) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun MediaGrind(medias: List<Media>?, onNavigateToMediaDetails: MediaItemClick) {
    if (medias == null || medias.isEmpty()) {
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
fun SearchIcon() {
    Icon(
        tint = AccentColor,
        imageVector = Icons.Rounded.Search,
        contentDescription = stringResource(R.string.search_icon)
    )
}
