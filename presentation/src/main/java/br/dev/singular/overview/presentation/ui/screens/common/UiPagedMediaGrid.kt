package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid

/**
 * A composable that displays a grid of media content based on the Paging 3 [LoadState].
 * It handles loading, error, and empty states.
 *
 * @param items The paginated media items to display.
 * @param tagPath The path for analytics tagging.
 * @param hasFilters A boolean indicating whether any filters are currently applied,
 * to customize the "nothing found" message.
 * @param gridState The state object to be used to control or observe the grid's state.
 * @param onRefresh A callback to be invoked to retry loading data in case of an error.
 * @param nothingFoundScreen A composable to be displayed when no media items are found.
 * @param errorScreen A composable to be displayed when an error occurs while loading data.
 * @param onClickItem A callback to be invoked when a media item is clicked.
 */
@Composable
fun UiPagedMediaGrid(
    items: LazyPagingItems<MediaUiModel>,
    tagPath: String,
    hasFilters: Boolean = false,
    gridState: LazyGridState = LazyGridState(),
    onRefresh: () -> Unit = {},
    nothingFoundScreen: @Composable () -> Unit = {
        NothingFoundScreen(tagPath, hasFilters = hasFilters)
    },
    errorScreen: @Composable () -> Unit = {
        ErrorScreen(tagPath, onRefresh = onRefresh)
    },
    onClickItem: (MediaUiModel) -> Unit
) {
    when (items.loadState.refresh) {
        is LoadState.Loading -> MediaGridSkeletonScreen(tagPath)
        is LoadState.Error -> errorScreen()
        is LoadState.NotLoading -> {
            if (items.itemCount > 0) {
                TrackScreenView(tagPath, TagStatus.SUCCESS)
                UiMediaGrid(items = items, gridState = gridState, onClick = onClickItem)
            } else {
                nothingFoundScreen()
            }
        }
    }
}

/**
 * A composable that displays a grid of media content based on the Paging 3 [LoadState].
 * This version includes an [initialScreen] parameter, which is displayed when [showInitial] is true
 * and the content is not in a loading or error state.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param items The paginated media items to display.
 * @param tagPath The path for analytics tagging.
 * @param showInitial A boolean indicating whether the [initialScreen] should be displayed
 * instead of the grid or empty state when not loading.
 * @param hasFilters A boolean indicating whether any filters are currently applied,
 * to customize the "nothing found" message.
 * @param gridState The state object to be used to control or observe the grid's state.
 * @param onRefresh A callback to be invoked to retry loading data in case of an error.
 * @param initialScreen A composable to be displayed when [showInitial] is true and the state is NotLoading.
 * @param nothingFoundScreen A composable to be displayed when no media items are found.
 * @param errorScreen A composable to be displayed when an error occurs while loading data.
 * @param onClickItem A callback to be invoked when a media item is clicked.
 */
@Composable
fun UiPagedMediaGrid(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<MediaUiModel>,
    tagPath: String,
    showInitial: Boolean = true,
    hasFilters: Boolean = false,
    gridState: LazyGridState = LazyGridState(),
    onRefresh: () -> Unit = {},
    initialScreen: @Composable () -> Unit,
    nothingFoundScreen: @Composable () -> Unit = {
        NothingFoundScreen(tagPath, hasFilters = hasFilters)
    },
    errorScreen: @Composable () -> Unit = {
        ErrorScreen(tagPath, onRefresh = onRefresh)
    },
    onClickItem: (MediaUiModel) -> Unit
) {
    when (items.loadState.refresh) {
        is LoadState.Loading -> MediaGridSkeletonScreen(
            modifier = modifier,
            tagPath = tagPath,
        )

        is LoadState.Error -> errorScreen()
        is LoadState.NotLoading -> {
            if (showInitial) {
                initialScreen()
            } else {
                if (items.itemCount > 0) {
                    TrackScreenView(tagPath, TagStatus.SUCCESS)
                    UiMediaGrid(
                        modifier = modifier,
                        items = items,
                        gridState = gridState,
                        onClick = onClickItem
                    )
                } else {
                    nothingFoundScreen()
                }
            }
        }
    }
}
