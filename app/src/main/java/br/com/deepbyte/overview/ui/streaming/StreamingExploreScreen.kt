package br.com.deepbyte.overview.ui.streaming

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.search.SearchIcon
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground

@Composable
fun StreamingExploreScreen(
    streaming: Streaming,
    events: BasicsMediaEvents,
    viewModel: StreamingExploreViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.StreamingExplore, tracker = viewModel.analyticsTracker)

    val loadData = { viewModel.getPaging(streamingApiId = streaming.apiId) }
    var items by remember { mutableStateOf(value = loadData()) }

    MediaExploreContent(
        events = events,
        streaming = streaming,
        showAds = viewModel.showAds,
        onRefresh = { items = loadData() },
        pagingItems = items.collectAsLazyPagingItems()
    )
}

@Composable
fun MediaExploreContent(
    showAds: Boolean,
    streaming: Streaming,
    onRefresh: () -> Unit,
    events: BasicsMediaEvents,
    pagingItems: LazyPagingItems<Media>
) {
    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
        is LoadState.NotLoading -> {
            Scaffold(
                modifier = Modifier
                    .background(PrimaryBackground)
                    .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
                topBar = {
                    StreamingToolBar(
                        streaming = streaming,
                        onClickBackIcon = events::onPopBackStack,
                        onClickSearchIcon = {}
                    )
                },
                bottomBar = {
                    AdsBanner(R.string.discover_banner, showAds)
                }
            ) { padding ->
                if (pagingItems.itemCount == 0) {
                    ErrorOnLoading()
                } else {
                    var selected by remember { mutableStateOf(MediaType.ALL.key) }
                    Column(Modifier.background(PrimaryBackground)) {
                        MediaTypeSelector(selected) { newSelected ->
                            selected = newSelected
                        }
                        VerticalSpacer(dimensionResource(R.dimen.screen_padding))
                        MediaPagingVerticalGrid(
                            padding,
                            pagingItems,
                            events::onNavigateToMediaDetails
                        )
                    }
                }
            }
        } else -> ErrorScreen(onRefresh)
    }
}

@Composable
fun StreamingToolBar(
    streaming: Streaming,
    onClickBackIcon: () -> Unit,
    onClickSearchIcon: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(bottom = dimensionResource(R.dimen.screen_padding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
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
            ) { onClickBackIcon.invoke() }
            HorizontalSpacer()
            StreamingIcon(streaming = streaming, withBorder = false)
            StreamingScreamTitle(streamingName = streaming.name)
        }
        SearchIcon(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.screen_padding))
                .clickable { onClickSearchIcon.invoke() }
        )
    }
}

@Composable
fun StreamingScreamTitle(streamingName: String) {
    Text(
        text = streamingName,
        color = AccentColor,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.screen_padding),
            vertical = dimensionResource(R.dimen.default_padding)
        ),
        overflow = TextOverflow.Ellipsis
    )
}
