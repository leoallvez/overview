package br.dev.singular.overview.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagSearch
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiCenteredColumn
import br.dev.singular.overview.presentation.ui.components.UiDivider
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiSearchField
import br.dev.singular.overview.presentation.ui.components.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.navigation.INavigationWrapper
import br.dev.singular.overview.presentation.ui.screens.common.MediaGridSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.MediaListSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.NothingFoundScreen
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection
import br.dev.singular.overview.presentation.ui.utils.rememberLazyGridScrollState
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.getStringByName
import kotlinx.collections.immutable.toImmutableList

private fun tagClick(detail: String, id: Long = 0L) {
    TagManager.logClick(TagSearch.PATH, detail, id)
}

@Composable
fun SearchScreen(
    navigate: INavigationWrapper,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val filters = viewModel.searchFilters.collectAsState().value
    val items = viewModel.mediasSearch.collectAsLazyPagingItems()
    val suggestionsUIState = viewModel.suggestionsUIState.collectAsState().value

    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    val scrollState by viewModel.scrollState.collectAsState()

    val gridState = rememberLazyGridScrollState(
        state = scrollState,
        onSet = viewModel::onSetScrollState
    )

    LaunchedEffect(Unit) {
        viewModel.onLoadSuggestions()
    }

    UiScaffold(
        padding = PaddingValues(),
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            SearchToolBar(filters.query) { query ->
                viewModel.onSearching(filters.copy(query = query))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(top = padding.calculateTopPadding())
        ) {
            UiMediaTypeSelector(
                visible = filters.query.isNotEmpty() && !isCollapsed,
                type = MediaUiType.getByName(name = filters.mediaType.key),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
                    .padding(bottom = dimensionResource(R.dimen.spacing_4x))
            ) {
                val newType = MediaType.getByKey(it.name.lowercase())
                TagMediaManager.logTypeClick(TagSearch.PATH, it)
                viewModel.onSearching(filters.copy(mediaType = newType))
            }
            UiDivider(
                visible = isCollapsed,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
            )
            Box {
                when (items.loadState.refresh) {
                    is LoadState.Loading -> MediaGridSkeletonScreen(
                        tagPath = TagSearch.PATH,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
                    )

                    is LoadState.NotLoading -> {
                        TrackScreenView(TagSearch.PATH, TagStatus.SUCCESS)
                        UiMediaGrid(
                            items = items,
                            gridState = gridState,
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.spacing_4x)),
                            onClick = {
                                TagMediaManager.logMediaClick(TagSearch.PATH, it.id)
                                navigate.toMediaDetails(it)
                            }
                        )
                    }

                    else -> {
                        if (items.itemCount == 0 && filters.query.isNotEmpty()) {
                            NothingFoundScreen(TagSearch.PATH)
                        } else {
                            SearchInitialScreen(
                                suggestions = suggestionsUIState,
                                tagPath = TagSearch.PATH_SUGGESTIONS,
                                onClick = {
                                    TagMediaManager.logMediaClick(TagSearch.PATH_SUGGESTIONS, it.id)
                                    navigate.toMediaDetails(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchToolBar(query: String, onSearch: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
            .padding(bottom = dimensionResource(R.dimen.spacing_4x))
    ) {
        UiTopAppBar(title = stringResource(id = R.string.search))
        UiSearchField(
            query = query,
            placeholder = stringResource(R.string.search_in_all_places),
            onQueryChange = {
                TagManager.logInteraction(TagSearch.PATH, TagSearch.Detail.SEARCH_FIELD)
                onSearch(it)
            },
            onClear = {
                tagClick(TagSearch.Detail.CLEAN_SEARCH_FIELD)
            }
        )
    }
}

@Composable
fun SearchInitialScreen(
    suggestions: SuggestionUIState,
    tagPath: String,
    onClick: (MediaUiModel) -> Unit
) {
    when (suggestions) {
        is UiState.Loading -> MediaListSkeletonScreen(
            tagPath = tagPath,
            contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
        )

        is UiState.Success -> {
            TrackScreenView(tagPath, TagStatus.SUCCESS)
            SuggestionsVerticalList(suggestions = suggestions.data, onClick = onClick)
        }

        is UiState.Error -> {
            TrackScreenView(tagPath, TagStatus.ERROR)
            UiCenteredColumn {
                UiTitle(
                    text = stringResource(R.string.search_not_started),
                    color = HighlightColor
                )
            }
        }
    }
}

@Composable
fun SuggestionsVerticalList(
    suggestions: Map<String, List<MediaUiModel>>,
    onClick: (MediaUiModel) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        itemsIndexed(items = suggestions.toList()) { _, (titleKey, mediaItems) ->
            UiMediaList(
                title = context.getStringByName(titleKey).orEmpty(),
                contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
                items = mediaItems.toImmutableList(),
                onClick = onClick
            )
        }
    }
}
