package io.github.leoallvez.take.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.leoallvez.take.data.MediaType
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.BasicImage
import io.github.leoallvez.take.ui.BasicText
import io.github.leoallvez.take.ui.UiStateResult
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun DiscoverScreen(
    providerId: Long,
    providerName: String,
    viewModel: DiscoverViewModel = hiltViewModel(),
    onNavigateToMediaDetails: MediaItemClick
) {

    viewModel.loadDada(providerId = providerId, mediaType = MediaType.TV.key)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = {
            viewModel.loadDada(providerId = providerId, mediaType = MediaType.TV.key)
        }
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
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxSize()
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {
            items(pagingItems.itemCount) { index ->
                GridItemMedia(
                    mediaItem = pagingItems[index],
                    onClick = onNavigateToMediaDetails
                )
            }
        }
    }
}

@Composable
fun GridItemMedia(mediaItem: MediaItem?, onClick: MediaItemClick) {
    mediaItem?.apply {
        Column(
            modifier = Modifier.padding(2.dp)
                .clickable { onClick(apiId, type) }
        ) {
            BasicImage(
                url = getPosterImage(),
                contentDescription = getLetter(),
                withBorder = true,
                modifier = Modifier.padding(3.dp)
            )
            BasicText(
                text = getLetter(),
                style = MaterialTheme.typography.caption,
                isBold = true
            )
        }
    }
}
