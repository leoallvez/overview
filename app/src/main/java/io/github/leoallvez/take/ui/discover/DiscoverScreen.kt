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
import io.github.leoallvez.take.ui.LoadingScreen
import io.github.leoallvez.take.ui.PagingUiState
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel(),
    onNavigateToMediaDetails: MediaItemClick
) {

    viewModel.loadDada(providerId = 337, mediaType = MediaType.TV.key)

    when (viewModel.uiState.collectAsState().value) {
        is PagingUiState.Loading -> LoadingScreen()
        is PagingUiState.Success -> DiscoverContent(
            pagingItems = viewModel.dataResult.collectAsLazyPagingItems(),
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
