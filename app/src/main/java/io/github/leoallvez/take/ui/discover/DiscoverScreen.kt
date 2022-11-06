package io.github.leoallvez.take.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import io.github.leoallvez.take.data.model.DiscoverParams
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun DiscoverScreen(
    params: DiscoverParams?,
    onNavigateToMediaDetails: MediaItemClick,
    viewModel: DiscoverViewModel = hiltViewModel(),
) {
    val loadData = {
        viewModel.loadDada(providerId = params?.providerId ?: 0, mediaType = MediaType.TV.key)
    }

    loadData.invoke()

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { loadData.invoke() }
    ) { dataResult ->
        DiscoverContent(
            providerName = params?.providerName ?: "",
            pagingItems = dataResult.collectAsLazyPagingItems(),
            onNavigatePopBackStack = {
                val mediaId = params?.mediaId ?: 0
                val mediaType = params?.mediaType ?: ""
                onNavigateToMediaDetails.invoke(mediaId, mediaType)
            },
            onNavigateToMediaDetails = onNavigateToMediaDetails,
        ) {
            loadData.invoke()
        }
    }
}

@Composable
fun DiscoverContent(
    providerName: String,
    pagingItems: LazyPagingItems<MediaItem>,
    onNavigatePopBackStack: () -> Unit,
    onNavigateToMediaDetails: MediaItemClick,
    onRefresh: () -> Unit
) {
    if (pagingItems.itemCount == 0) {
        ErrorScreen { onRefresh.invoke() }
    } else {
        Scaffold(
            modifier = Modifier.background(Color.Black),
            topBar = {
                DiscoverToolBar(providerName, onNavigatePopBackStack)
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
    Row(Modifier.fillMaxWidth().background(Color.Black)) {
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon
        ) { backButtonAction.invoke() }
        Text(text = providerName)
    }
}

@Composable
fun GridItemMedia(mediaItem: MediaItem?, onClick: MediaItemClick) {
    mediaItem?.apply {
        Column(
            modifier = Modifier
                .padding(2.dp)
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
