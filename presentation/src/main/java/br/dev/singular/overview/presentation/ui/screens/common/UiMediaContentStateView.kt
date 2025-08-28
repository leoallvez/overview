package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid

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
        is LoadState.NotLoading -> {
            if (pagedMedias.itemCount > 0) {
                TrackScreenView(tagPath, TagStatus.SUCCESS)
                UiMediaGrid(items = pagedMedias, onClick = onClickItem)
            } else {
                errorScreen()
            }
        }
        else -> nothingFoundScreen()
    }
}
