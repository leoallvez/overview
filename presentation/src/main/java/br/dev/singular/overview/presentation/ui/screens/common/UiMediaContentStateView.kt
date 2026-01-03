package br.dev.singular.overview.presentation.ui.screens.common

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
 * @param pagedMedias The paginated media items to display.
 * @param tagPath The path for analytics tagging.
 * @param hasFilters A boolean indicating whether any filters are currently applied,
 * to customize the "nothing found" message.
 * @param onRefresh A callback to be invoked to retry loading data in case of an error.
 * @param nothingFoundScreen A composable to be displayed when no media items are found.
 * @param errorScreen A composable to be displayed when an error occurs while loading data.
 * @param loadingScreen A composable to be displayed while the initial data is loading.
 * @param onClickItem A callback to be invoked when a media item is clicked.
 */
@Composable
fun UiMediaContentStateView(
    pagedMedias: LazyPagingItems<MediaUiModel>,
    tagPath: String,
    hasFilters: Boolean = false,
    onRefresh: () -> Unit = {},
    nothingFoundScreen: @Composable () -> Unit = {
        NothingFoundScreen(tagPath, hasFilters = hasFilters)
    },
    errorScreen: @Composable () -> Unit = {
        ErrorScreen(tagPath, onRefresh = onRefresh)
    },
    loadingScreen: @Composable () -> Unit = { LoadingScreen(tagPath) },
    onClickItem: (MediaUiModel) -> Unit
) {
    when (pagedMedias.loadState.refresh) {
        is LoadState.Loading -> loadingScreen()
        is LoadState.Error -> errorScreen()
        is LoadState.NotLoading -> {
            if (pagedMedias.itemCount > 0) {
                TrackScreenView(tagPath, TagStatus.SUCCESS)
                UiMediaGrid(items = pagedMedias, onClick = onClickItem)
            } else {
                nothingFoundScreen()
            }
        }
    }
}
