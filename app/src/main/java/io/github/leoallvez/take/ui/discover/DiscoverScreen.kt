package io.github.leoallvez.take.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.leoallvez.take.data.MediaType
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.GridItemMedia
import io.github.leoallvez.take.ui.UiStateResult
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun DiscoverScreen(
    providerId: Long,
    viewModel: DiscoverViewModel = hiltViewModel(),
    onNavigateToMediaDetails: MediaItemClick
) {

    viewModel.loadDada(providerId = providerId, mediaType = MediaType.TV.key)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { /**viewModel.refresh(apiId, mediaType)*/ }
    ) { dataResult ->
        DiscoverContent(
            pagingItems = dataResult.collectAsLazyPagingItems(),
            onNavigateToMediaDetails = onNavigateToMediaDetails
        )
    }
}

@Composable
fun DiscoverContent(
    pagingItems: LazyPagingItems<MediaItem>,
    onNavigateToMediaDetails: MediaItemClick
) {
    Column(
        modifier = Modifier.background(PrimaryBackground).fillMaxSize()
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(pagingItems.itemCount) { index ->
                val item = pagingItems[index]
                item?.apply {
                    GridItemMedia(mediaItem = item, imageWithBorder = true) {
                        onNavigateToMediaDetails.invoke(apiId, type)
                    }
                }
            }
        }
    }
}
