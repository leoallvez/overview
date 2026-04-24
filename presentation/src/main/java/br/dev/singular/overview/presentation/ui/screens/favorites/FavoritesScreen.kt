package br.dev.singular.overview.presentation.ui.screens.favorites

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.ui.components.UiDivider
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.screens.common.StateScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiPagedMediaGrid
import br.dev.singular.overview.presentation.ui.screens.favorites.interaction.FavoritesActions
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import br.dev.singular.overview.presentation.ui.utils.fakeQueryState
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection
import br.dev.singular.overview.presentation.ui.utils.rememberLazyGridScrollState

/**
 * A composable that displays the user's favorite media.
 *
 * @param queryState The state of the query (filters).
 * @param scrollState The state of the scroll position.
 * @param uiPages The paginated list of favorite media items.
 * @param onSetScrollState Callback to update the scroll state.
 * @param actions The actions to be performed on the screen.
 */
@Composable
fun FavoritesScreen(
    queryState: QueryUiState,
    scrollState: ScrollUiState,
    uiPages: LazyPagingItems<MediaUiModel>,
    onSetScrollState: (ScrollUiState) -> Unit = {},
    actions: FavoritesActions = FavoritesActions(),
) {

    val gridState = rememberLazyGridScrollState(scrollState, onSetScrollState)

    FavoritesContent(
        actions = actions,
        queryState = queryState,
    ) {
        UiPagedMediaGrid(
            tagPath = actions.tagPath,
            items = uiPages,
            gridState = gridState,
            errorScreen = { EmptyFavoritesScreen(actions.tagPath, queryState.type) },
            onClickItem = { media ->
                actions.onToMediaDetails(media)
            }
        )
    }
}

@Composable
private fun FavoritesContent(
    actions: FavoritesActions,
    queryState: QueryUiState,
    content: @Composable () -> Unit
) {
    val isPreview = LocalInspectionMode.current
    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    UiScaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = { UiTopAppBar(title = stringResource(id = R.string.favorites)) }
    ) { padding ->
        Column(
            modifier = Modifier
                .then(
                    if (isPreview) Modifier
                    else Modifier.animateContentSize()
                )
                .padding(top = padding.calculateTopPadding())
        )
        {
            UiMediaTypeSelector(
                visible = !isCollapsed,
                type = queryState.type,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_4x))
            ) { type ->
                actions.onSelectType(type)
            }
            UiDivider(visible = isCollapsed)
            content()
        }
    }
}

@Composable
private fun EmptyFavoritesScreen(tagPath: String, type: MediaUiType) {
    StateScreen(
        title = stringResource(
            when (type) {
                MediaUiType.MOVIE -> R.string.liked_movie_not_found
                MediaUiType.TV -> R.string.liked_tv_show_not_found
                else -> R.string.liked_not_found
            }
        ),
        tagPath = tagPath,
        tagStatus = "nothing_found"
    )
}

@UiScreenPreview
@Composable
private fun FavoritesScreenPreview() {

    var queryState by remember { mutableStateOf(fakeQueryState()) }

    FavoritesContent(
        queryState = queryState,
        actions = FavoritesActions(
            onSetType = {
                queryState = queryState.copy(type = it)
            }
        )
    ) {
        UiMediaGrid(items = fakeMedias(90))
    }
}
