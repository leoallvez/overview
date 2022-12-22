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
import br.com.deepbyte.overview.data.MediaType.MOVIE
import br.com.deepbyte.overview.data.MediaType.TV_SHOW
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
import timber.log.Timber

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    Scaffold(
        backgroundColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            SearchToolBar(onNavigateToHome) { query ->
                viewModel.search(query)
            }
        },
        bottomBar = {
            AdsBanner(R.string.banner_sample_id, viewModel.showAds)
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (val uiState = viewModel.uiState.collectAsState().value) {
                is SearchState.NotStated -> SearchNotStated()
                is SearchState.Loading -> LoadingScreen()
                is SearchState.Success -> SearchSuccess(uiState.data)
                is SearchState.Empty -> SearchEmpty()
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
fun SearchNotStated() {
    Text("SearchNotStated", color = Color.White)
}

@Composable
fun SearchSuccess(results: Map<String, List<Media>>) {

    var selected by remember { mutableStateOf(MOVIE.key) }

    Column {
        MediaSelector(selected) { newSelected ->
            selected = newSelected
        }
        MediaGrind(medias = results[selected])
    }
}

@Composable
fun MediaSelector(selector: String, onClick: (String) -> Unit) {
    val options = listOf(MOVIE, TV_SHOW)
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
    val color = if (isActivated) AccentColor else Color.Gray
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
fun MediaGrind(medias: List<Media>?) {
    if (medias != null && medias.isNotEmpty()) {
        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {
                items(medias.size) { index ->
                    GridItemMedia(
                        media = medias[index],
                        onClick = { media ->
                            Timber.tag("click_media").i("media title: ${media.getLetter()}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchEmpty() {
    Text("SearchEmpty", color = Color.White)
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
        contentDescription = stringResource(R.string.search_icon),
    )
}
