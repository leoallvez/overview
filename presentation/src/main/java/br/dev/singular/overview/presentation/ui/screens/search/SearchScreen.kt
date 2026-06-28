package br.dev.singular.overview.presentation.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiCenteredColumn
import br.dev.singular.overview.presentation.ui.components.UiDivider
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiSearchField
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.screens.common.MediaListSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.presentation.ui.screens.common.UiPagedMediaGrid
import br.dev.singular.overview.presentation.ui.screens.search.interaction.SearchActions
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import br.dev.singular.overview.presentation.ui.utils.fakeQueryState
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection
import br.dev.singular.overview.presentation.ui.utils.rememberLazyGridScrollState
import br.dev.singular.overview.presentation.util.getStringByName
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SearchScreen(
    queryState: QueryUiState,
    scrollState: ScrollUiState,
    uiPages: LazyPagingItems<MediaUiModel>,
    suggestionsUIState: SuggestionUIState,
    onSetScrollState: (ScrollUiState) -> Unit = {},
    actions: SearchActions
) {

    val gridState = rememberLazyGridScrollState(
        state = scrollState,
        onSet = onSetScrollState
    )

    LaunchedEffect(Unit) {
        actions.onLoadSuggestions()
    }

    SearchContent(
        actions = actions,
        queryState = queryState,
    ) {
        UiPagedMediaGrid(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_4x)),
            tagPath = actions.tagPath.search,
            items = uiPages,
            gridState = gridState,
            onClickItem = {
                actions.onToMediaDetails(media = it)
            },
            showInitial = queryState.query.isEmpty(),
            initialScreen = {
                SuggestionScreen(
                    suggestions = suggestionsUIState,
                    tagPath = actions.tagPath.suggestion,
                    onClick = {
                        actions.onToMediaDetails(media = it, isSuggestion = true)
                    }
                )
            }
        )
    }
}

@Composable
private fun SearchContent(
    actions: SearchActions,
    queryState: QueryUiState,
    content: @Composable () -> Unit
) {
    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    UiScaffold(
        padding = PaddingValues(),
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            SearchTopBar(
                query = queryState.query,
                onSearch = actions::onSearch,
                onClear = actions::onClear
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
        ) {
            UiMediaTypeSelector(
                visible = queryState.query.isNotEmpty() && !isCollapsed,
                type = queryState.type,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
                    .padding(bottom = dimensionResource(R.dimen.spacing_4x))
            ) {
                actions.onSelectType(it)
            }
            UiDivider(
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_4x)),
                visible = isCollapsed
            )
            content()
        }
    }
}

@Composable
private fun SearchTopBar(
    query: String,
    onSearch: (String) -> Unit,
    onClear: () -> Unit
) {
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
            onQueryChange = onSearch,
            onClear = onClear
        )
    }
}

@Composable
private fun SuggestionScreen(
    suggestions: SuggestionUIState,
    tagPath: String = "",
    onClick: (MediaUiModel) -> Unit = {}
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
private fun SuggestionsVerticalList(
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

@UiScreenPreview
@Composable
internal fun SearchScreenPreview() {
    SearchContent(
        queryState = fakeQueryState(),
        actions = SearchActions()
    ) {
        UiMediaGrid(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_4x)),
            items = fakeMedias(90)
        )
    }
}

@UiScreenPreview
@Composable
internal fun SuggestionScreenPreview() {
    val medias = fakeMedias()
    SearchContent(
        queryState = fakeQueryState(),
        actions = SearchActions()
    ) {
        SuggestionScreen(
            suggestions = UiState.Success(
                data = mapOf(
                    "tv_top_rated" to medias,
                    "tv_trending" to medias,
                    "movie_top_rated" to medias,
                    "discover_movie" to medias,
                    "movie_trending" to medias,
                    "movie_popular" to medias
                )
            )
        )
    }
}

@UiScreenPreview
@Composable
internal fun SuggestionScreenLoadingPreview() {
    SearchContent(
        queryState = fakeQueryState(),
        actions = SearchActions()
    ) {
        SuggestionScreen(
            suggestions = UiState.Loading()
        )
    }
}

@UiScreenPreview
@Composable
internal fun SuggestionScreenErrorPreview() {
    SearchContent(
        queryState = fakeQueryState(),
        actions = SearchActions()
    ) {
        SuggestionScreen(
            suggestions = UiState.Error()
        )
    }
}
