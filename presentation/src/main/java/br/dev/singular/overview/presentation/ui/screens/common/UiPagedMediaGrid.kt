package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
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
