package br.com.deepbyte.overview.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.repository.search.SearchContents
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
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
fun SearchToolBar(backButtonAction: () -> Unit, onSearch: (String) -> Unit) {

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

        SearchField(onSearch)
    }
}

@Composable
fun SearchNotStated() {
    Text("SearchNotStated", color = Color.White)
}

@Composable
fun SearchSuccess(contents: SearchContents) {
    with(contents) {
        Column {
            // SearchMediaResults(title = "MOVIES", movies)
            SearchMediaResults(title = "TV SHOW", tvShows)
            // SearchPersonResults(title = "PERSON", persons)
        }
    }
}

@Composable
fun SearchMediaResults(title: String, medias: List<Media>) {
    if (medias.isNotEmpty()) {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(text = "$title [${medias.size}]", color = Color.White)
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
