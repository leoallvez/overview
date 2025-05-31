package br.dev.singular.overview.ui.search

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.R
import br.dev.singular.overview.presentation.UIState
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.presentation.ui.media.HorizontalMediaList
import br.dev.singular.overview.ui.DefaultVerticalSpace
import br.dev.singular.overview.ui.IntermediateScreensText
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaEntityPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeSelector
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SearchField
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.model.toMediaType
import br.dev.singular.overview.ui.navigation.wrappers.BasicNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.MediaItemClick
import br.dev.singular.overview.util.getStringByName

@Composable
fun SearchScreen(
    navigate: BasicNavigate,
    viewModel: SearchViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Search, viewModel.analyticsTracker)

    val filters = viewModel.searchFilters.collectAsState().value
    val items = viewModel.mediasSearch.collectAsLazyPagingItems()
    val suggestionsUIState = viewModel.suggestionsUIState.collectAsState().value

    Scaffold(
        contentColor = PrimaryBackground,
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            SearchToolBar { newQuery ->
                viewModel.onSearching(filters.copy(query = newQuery))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(top = padding.calculateTopPadding())
        ) {
            if (items.itemCount > 0 || filters.query.isNotEmpty()) {
                MediaTypeSelector(filters.mediaType.key) { newType ->
                    viewModel.onSearching(filters.copy(mediaType = newType))
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
                            SearchInitialScreen(suggestionsUIState, navigate::toMediaDetails)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchToolBar(onSearch: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.screen_padding))
    ) {
        ToolbarTitle(title = stringResource(id = R.string.search))
        DefaultVerticalSpace()
        SearchField(
            onSearch = onSearch,
            autoOpenKeyboard = false,
            defaultPaddingValues = PaddingValues(horizontal = 6.dp),
            placeholder = stringResource(R.string.search_in_all_places)
        )
    }
}

@Composable
fun SearchInitialScreen(suggestions: SuggestionUIState, onClickItem: MediaItemClick) {
    when (suggestions) {
        is UIState.Loading -> LoadingScreen()
        is UIState.Success -> {
            SuggestionsVerticalList(
                suggestions = suggestions.data,
                onClickItem = onClickItem
            )
        }
        is UIState.Error -> CenteredTextString(R.string.search_not_started)
    }
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

@Composable
fun SuggestionsVerticalList(
    suggestions: Map<String, List<MediaUIModel>>,
    onClickItem: MediaItemClick
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = dimensionResource(R.dimen.default_padding))
            .verticalScroll(rememberScrollState())
    ) {
        suggestions.forEach { (titleKey, mediaItems) ->
            HorizontalMediaList(
                title = context.getStringByName(titleKey).orEmpty(),
                items = mediaItems,
                onClick = { media -> onClickItem(media.id, media.type.toMediaType()) }
            )
        }
    }
}
