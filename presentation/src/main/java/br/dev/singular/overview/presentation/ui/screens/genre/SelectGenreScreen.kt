package br.dev.singular.overview.presentation.ui.screens.genre

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.GenreUiState
import br.dev.singular.overview.presentation.ui.components.UiList
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.genre.UiGenreItem
import br.dev.singular.overview.presentation.ui.screens.common.ItemListSkeletonScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionActions
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeGenres
import kotlinx.collections.immutable.toImmutableList

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
    ) { padding ->
        UiStateResult(
            uiState = uiState,
            tagPath = actions.tagPath,
            onRefresh = { actions.onLoad() },
            modifier = Modifier.padding(padding),
            loadingContent = { ItemListSkeletonScreen(tagPath = actions.tagPath) },
        ) { data ->
            UiList(
                items = data.options.toImmutableList(),
                firstItem = {
                    AllGenreOption(
                        selected = data.selectedId == null,
                        onClick = { actions.onSelect(genre = null) }
                    )
                }
            ) { genre ->
                UiGenreItem(
                    model = genre,
                    selected = genre.id == data.selectedId,
                    onClick = { actions.onSelect(genre) }
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
private fun SelectGenreScreenSuccessPreview() {
    var selectedId by remember { mutableStateOf<Long?>(null) }
    val genres = fakeGenres()
    val uiState = remember(selectedId) {
        UiState.Success(
            data = GenreUiState(
                selectedId = selectedId,
                options = genres
            )
        )
    }
    SelectGenreScreen(
        uiState = uiState,
        actions = GenreSelectionActions(
            handleIntent = { intent ->
                if (intent is GenreSelectionIntent.Select) {
                    selectedId = intent.genre?.id
                }
            }
        )
    )
}

@UiScreenPreview
@Composable
private fun SelectGenreScreenLoadingPreview() {
    SelectGenreScreen(
        uiState = UiState.Loading(),
        actions = GenreSelectionActions()
    )
}

@UiScreenPreview
@Composable
private fun SelectGenreScreenErrorPreview() {
    SelectGenreScreen(
        uiState = UiState.Error(),
        actions = GenreSelectionActions()
    )
}
