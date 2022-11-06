package io.github.leoallvez.take.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.MediaType
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun DiscoverScreen(
    providerId: Long,
    providerName: String,
    viewModel: DiscoverViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToMediaDetails: MediaItemClick,
) {

    viewModel.loadDada(providerId = providerId, mediaType = MediaType.TV.key)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = {
            viewModel.loadDada(providerId = providerId, mediaType = MediaType.TV.key)
        }
    ) { dataResult ->
        DiscoverContent(
            providerName = providerName,
            pagingItems = dataResult.collectAsLazyPagingItems(),
            onNavigateToMediaDetails = onNavigateToMediaDetails
        ) {
            viewModel.loadDada(providerId = providerId, mediaType = MediaType.TV.key)
        }
    }
}

@Composable
fun DiscoverContent(
//    mediaDetails: MediaDetailResponse?,
//    showAds: Boolean,
//    events: MediaDetailsScreenEvents,
    providerName: String,
    pagingItems: LazyPagingItems<MediaItem>,
    onNavigateToMediaDetails: MediaItemClick,
    onRefresh: () -> Unit
) {
    if (pagingItems.itemCount == 0) {
        ErrorScreen { onRefresh.invoke() }
    } else {
        Scaffold(
            modifier = Modifier.background(Color.Black),
            topBar = {
                DiscoverToolBar(providerName) {}
            }
        ) { padding ->
            DiscoverBody(padding, pagingItems, onNavigateToMediaDetails)
        }
    }
}

@Composable
fun DiscoverBody(
    padding: PaddingValues,
    pagingItems: LazyPagingItems<MediaItem>,
    onNavigateToMediaDetails: MediaItemClick,
    // backButtonAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(padding)
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
fun DiscoverToolBar(providerName: String, backButtonAction: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        //mediaDetails.apply {
//            Backdrop(
//                url = getBackdropImage(),
//                contentDescription = getLetter(),
//                modifier = Modifier.align(Alignment.CenterEnd)
//            )
//            BasicImage(
//                url = getPosterImage(),
//                contentDescription = getLetter(),
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .size(width = 140.dp, height = 200.dp)
//                    .padding(dimensionResource(R.dimen.screen_padding))
//                    .shadow(2.dp, shape = RoundedCornerShape(dimensionResource(R.dimen.corner)))
//            )
            ToolbarButton(
                painter = Icons.Default.KeyboardArrowLeft,
                descriptionResource = R.string.back_to_home_icon
            ) { backButtonAction.invoke() }
        //}
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
