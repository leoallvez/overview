package br.com.deepbyte.overview.ui.streaming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.theme.PrimaryBackground

@Composable
fun StreamingExploreScreen(
    apiId: Long,
    events: BasicsMediaEvents,
    viewModel: StreamingExploreViewModel = hiltViewModel()
) {
    // TrackScreenView(screen = ScreenNav.StreamingExplore, tracker = viewModel.analyticsTracker)

    val loadData = { viewModel.getPaging(streamingApiId = apiId) }
    var items by remember { mutableStateOf(value = loadData()) }

    MediaExploreContent(
        showAds = true, // viewModel.showAds,
        providerName = "Streaming Name",
        pagingItems = items.collectAsLazyPagingItems(),
        onRefresh = { items = loadData() },
        onPopBackStack = { events.onPopBackStack() },
        onClickItem = {
            events.onNavigateToMediaDetails(0, "")
        }
    )
}

@Composable
fun MediaExploreContent(
    showAds: Boolean,
    providerName: String,
    pagingItems: LazyPagingItems<Media>,
    onRefresh: () -> Unit,
    onPopBackStack: () -> Unit,
    onClickItem: () -> Unit
) {
    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
        is LoadState.NotLoading -> {
            Scaffold(
                modifier = Modifier
                    .background(PrimaryBackground)
                    .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
                topBar = { DiscoverToolBar(providerName, onPopBackStack) },
                bottomBar = {
                    AdsBanner(R.string.discover_banner, showAds)
                }
            ) { padding ->
                if (pagingItems.itemCount == 0) {
                    ErrorOnLoading()
                } else {
                    MediaExploreBody(padding, pagingItems, onClickItem)
                }
            }
        }
        else -> ErrorScreen(onRefresh)
    }
}

@Composable
fun MediaExploreBody(
    padding: PaddingValues,
    pagingItems: LazyPagingItems<Media>,
    onClickItem: () -> Unit = {}
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
                    media = pagingItems[index] as Media,
                    onClick = { onClickItem.invoke() }
                )
            }
        }
    }
}

@Composable
fun DiscoverToolBar(screenTitle: String, backButtonAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon,
            background = Color.White.copy(alpha = 0.1f),
            padding = PaddingValues(
                vertical = dimensionResource(R.dimen.screen_padding),
                horizontal = 2.dp
            )
        ) { backButtonAction.invoke() }
        ScreenTitle(text = screenTitle)
    }
}
