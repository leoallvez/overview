package br.dev.singular.overview.presentation.ui.screens.catalog.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.UiDivider
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiSearchField
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogTopAppBar
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.screens.catalog.components.UiMainFilter
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailActions
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailIntent
import br.dev.singular.overview.presentation.ui.screens.common.UiPagedMediaGrid
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import br.dev.singular.overview.presentation.ui.utils.fakeQueryState
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection
import br.dev.singular.overview.presentation.ui.utils.rememberLazyGridScrollState
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X

/**
 * Displays the details of a catalog, including a list of its media items.
 *
 * @param queryState The [QueryUiState] that holds the current screen state.
 * @param scrollState The [ScrollUiState] used to restore and save the scroll position.
 * @param uiPages The paginated list of [MediaUiModel] items to be displayed in the grid.
 * @param actions The [CatalogDetailActions] containing the event handlers for the screen.
 */
@Composable
fun CatalogDetailsScreen(
    queryState: QueryUiState,
    scrollState: ScrollUiState,
    uiPages: LazyPagingItems<MediaUiModel>,
    actions: CatalogDetailActions,
) {
    val gridState = rememberLazyGridScrollState(scrollState, actions::onSetScrollState)

    CatalogDetailsContent(
        queryState = queryState,
        actions = actions,
    ) {
        UiPagedMediaGrid(
            tagPath = actions.tagPath,
            items = uiPages,
            hasFilters = queryState.genre != null || queryState.query.isNotBlank(),
            gridState = gridState,
            onClickItem = actions::navigateToMediaDetails
        )
    }
}

/**
 * The internal content of the catalog details screen.
 *
 * @param queryState The [QueryUiState] that holds the current screen state.
 * @param actions The [CatalogDetailActions] containing the event handlers.
 * @param content The composable content representing the media items grid.
 */
@Composable
private fun CatalogDetailsContent(
    queryState: QueryUiState,
    actions: CatalogDetailActions,
    content: @Composable () -> Unit
) {
    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    val spacing = dimensionResource(R.dimen.spacing_4x)
    val isFilterVisible = !isCollapsed && queryState.catalog != null

    UiScaffold(
        padding = PaddingValues(),
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            CatalogDetailsTopAppBar(
                queryState = queryState,
                isCollapsed = isCollapsed,
                actions = actions
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .animateContentSize()
        ) {
            UiMainFilter(
                query = queryState,
                visible = isFilterVisible,
                modifier = Modifier.padding(bottom = spacing),
                contentPadding = PaddingValues(start = spacing),
                onClickFilter = actions::onFilterClick
            )
            Column(
                modifier = Modifier.padding(horizontal = spacing)
            ) {
                UiDivider(visible = isCollapsed)
                content()
            }
        }
    }
}

/**
 * The top app bar of the catalog details screen, with the search toggle and the search field.
 *
 * @param queryState The [QueryUiState] that holds the current screen state.
 * @param isCollapsed Whether the top app bar is in its collapsed state.
 * @param actions The [CatalogDetailActions] containing the event handlers.
 */
@Composable
private fun CatalogDetailsTopAppBar(
    queryState: QueryUiState,
    isCollapsed: Boolean,
    actions: CatalogDetailActions
) {
    val hasQuery = queryState.query.isNotEmpty()
    var isSearchOpen by rememberSaveable { mutableStateOf(hasQuery) }
    var shouldFocusSearch by remember { mutableStateOf(false) }

    LaunchedEffect(hasQuery) {
        if (hasQuery) isSearchOpen = true
    }

    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_4x))) {
        UiCatalogTopAppBar(
            catalog = queryState.catalog,
            isCollapsed = isCollapsed,
            trailingContent = {
                SearchToggleButton(
                    isSearchOpen = isSearchOpen,
                    onClick = {
                        isSearchOpen = !isSearchOpen
                        shouldFocusSearch = isSearchOpen
                        actions.onToggleSearch(isSearchOpen)
                    }
                )
            }
        )
        CatalogSearchField(
            query = queryState.query,
            isVisible = isSearchOpen,
            shouldFocus = shouldFocusSearch,
            actions = actions
        ) { shouldFocusSearch = false }
    }
}

/**
 * The animated search field of the catalog details screen.
 *
 * @param query The current search query.
 * @param isVisible Whether the search field is displayed.
 * @param shouldFocus Whether the search field must request the focus when displayed.
 * @param actions The [CatalogDetailActions] containing the event handlers.
 * @param onFocused Callback invoked after the search field requests the focus.
 */
@Composable
private fun CatalogSearchField(
    query: String,
    isVisible: Boolean,
    shouldFocus: Boolean,
    actions: CatalogDetailActions,
    onFocused: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    UiAnimatedVisibility(visible = isVisible) {
        LaunchedEffect(shouldFocus) {
            if (shouldFocus) {
                focusRequester.requestFocus()
                onFocused()
            }
        }
        UiSearchField(
            query = query,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.spacing_4x))
                .focusRequester(focusRequester),
            placeholder = stringResource(R.string.search_in_catalog),
            onQueryChange = actions::onSearch,
            onClear = actions::onClear
        )
    }
}

/**
 * The button that opens and closes the search field of the catalog details screen.
 *
 * @param isSearchOpen Whether the search field is displayed, which defines the icon of the button.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
private fun SearchToggleButton(isSearchOpen: Boolean, onClick: () -> Unit) {

    val (icon, descriptionRes) = if (isSearchOpen) {
        Lucide.X to R.string.close
    } else {
        Lucide.Search to R.string.search_icon
    }

    UiIconButton(
        iconStyle = UiIconStyle(
            source = UiIconSource.vector(icon),
            sizeRes = R.dimen.spacing_5x,
            descriptionRes = descriptionRes
        ),
        onClick = onClick
    )
}

@UiScreenPreview
@Composable
internal fun CatalogDetailsContentPreview() {

    val queryState = remember { mutableStateOf(fakeQueryState()) }

    CatalogDetailsContent(
        queryState = queryState.value,
        actions = CatalogDetailActions(
            handleIntent = { intent ->
                if (intent is CatalogDetailIntent.SelectType) {
                    queryState.value = queryState.value.copy(type = intent.type)
                }
            }
        ),
    ) {
        UiMediaGrid(items = fakeMedias(90))
    }
}

@UiScreenPreview
@Composable
internal fun CatalogDetailsContentSearchPreview() {

    val queryState = remember { mutableStateOf(fakeQueryState().copy(query = "Lorem ipsum")) }

    CatalogDetailsContent(
        queryState = queryState.value,
        actions = CatalogDetailActions(
            handleIntent = { intent ->
                when (intent) {
                    is CatalogDetailIntent.SelectType ->
                        queryState.value = queryState.value.copy(type = intent.type)

                    is CatalogDetailIntent.Search ->
                        queryState.value = queryState.value.copy(query = intent.query)

                    else -> Unit
                }
            }
        ),
    ) {
        UiMediaGrid(items = fakeMedias(90))
    }
}
