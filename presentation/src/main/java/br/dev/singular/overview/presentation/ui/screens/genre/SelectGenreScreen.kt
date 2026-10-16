package br.dev.singular.overview.presentation.ui.screens.genre

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.GenreUiState
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.UiList
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiSearchField
import br.dev.singular.overview.presentation.ui.components.button.UiConfirmButton
import br.dev.singular.overview.presentation.ui.components.genre.UiGenreItem
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.screens.common.ItemListSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.StateScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionActions
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeGenres

/**
 * A screen that allows the user to select a genre.
 *
 * @param uiState The state of the UI, which can be loading, success, or error.
 * @param actions The actions to be performed on the screen, such as loading data or selecting an item.
 */
@Composable
fun SelectGenreScreen(
    uiState: UiState<GenreUiState>,
    actions: GenreSelectionActions,
) {
    LaunchedEffect(Unit) { actions.onLoad() }
    UiScaffold(
        topBar = {
            UiTopAppBar(
                title = stringResource(R.string.filter_by_genre),
                onBack = { actions.onBack() }
            )
        },
        bottomBar = {
            if (uiState !is UiState.Success) return@UiScaffold
            UiConfirmButton(
                enabled = uiState.data.hasChanged,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.spacing_2x),
                    bottom = dimensionResource(R.dimen.spacing_1x)
                ),
                onClick = {
                    actions.onSelect(genre = uiState.data.selected)
                }
            )
        }
    ) { padding ->
        UiStateResult(
            uiState = uiState,
            tagPath = actions.tagPath,
            onRefresh = { actions.onLoad() },
            modifier = Modifier.padding(padding),
            loadingContent = { ItemListSkeletonScreen(tagPath = actions.tagPath) },
        ) { data ->
            GenreList(
                data = data,
                actions = actions
            )
        }
    }
}

/**
 * The list of genres, with the search field displayed when there are many genres.
 *
 * @param data The [GenreUiState] with the genres, the selected genre and the search query.
 * @param actions The actions to be performed on the screen.
 */
@Composable
private fun GenreList(
    data: GenreUiState,
    actions: GenreSelectionActions
) {
    Column {
        UiAnimatedVisibility(visible = data.isSearchVisible) {
            UiSearchField(
                query = data.query,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_2x)),
                placeholder = stringResource(R.string.search_in_genres),
                onQueryChange = actions::onSearch,
                onClear = actions::onClear
            )
        }
        if (data.isSearching && data.filteredOptions.isEmpty()) {
            StateScreen(
                title = stringResource(R.string.not_found),
                tagPath = actions.tagPath,
                tagStatus = "nothing_found"
            )
        } else {
            UiList(
                items = data.filteredOptions,
                firstItem = {
                    if (!data.isSearching) {
                        AllGenreOption(
                            selected = data.selected?.id == null,
                            onClick = { actions.onUpdate(genre = null) }
                        )
                    }
                }
            ) { genre ->
                UiGenreItem(
                    model = genre,
                    selected = genre.id == data.selected?.id,
                    onClick = { actions.onUpdate(genre) }
                )
            }
        }
    }
}

@Composable
private fun AllGenreOption(
    selected: Boolean,
    onClick: () -> Unit
) {
    UiGenreItem(
        model = GenreUiModel(name = stringResource(R.string.all)),
        selected = selected,
        onClick = onClick
    )
}

@UiScreenPreview
@Composable
internal fun SelectGenreScreenSuccessPreview() {
    val selectedId = remember { mutableStateOf<Long?>(null) }
    val genres = fakeGenres()
    val uiState = remember(selectedId.value) {
        UiState.Success(
            data = GenreUiState(
                selected = genres.first(),
                initial = genres.first(),
                options = genres
            )
        )
    }
    SelectGenreScreen(
        uiState = uiState,
        actions = GenreSelectionActions(
            handleIntent = { intent ->
                if (intent is GenreSelectionIntent.Select) {
                    selectedId.value = intent.genre?.id
                }
            }
        )
    )
}

@UiScreenPreview
@Composable
internal fun SelectGenreScreenSearchPreview() {
    val query = remember { mutableStateOf("an") }
    val genres = fakeGenres()
    SelectGenreScreen(
        uiState = UiState.Success(
            data = GenreUiState(
                selected = genres.first(),
                initial = genres.first(),
                options = genres,
                query = query.value
            )
        ),
        actions = GenreSelectionActions(
            handleIntent = { intent ->
                if (intent is GenreSelectionIntent.Search) {
                    query.value = intent.query
                }
            }
        )
    )
}

@UiScreenPreview
@Composable
internal fun SelectGenreScreenLoadingPreview() {
    SelectGenreScreen(
        uiState = UiState.Loading(),
        actions = GenreSelectionActions()
    )
}

@UiScreenPreview
@Composable
internal fun SelectGenreScreenErrorPreview() {
    SelectGenreScreen(
        uiState = UiState.Error(),
        actions = GenreSelectionActions()
    )
}
