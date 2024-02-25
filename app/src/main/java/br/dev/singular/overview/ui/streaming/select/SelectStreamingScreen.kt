package br.dev.singular.overview.ui.streaming.select

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.R
import br.dev.singular.overview.data.model.provider.StreamingData
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.BasicImage
import br.dev.singular.overview.ui.DisabledSearchToolBar
import br.dev.singular.overview.ui.STREAMING_GRID_COLUMNS
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SimpleTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.navigation.wrappers.SelectStreamingNavigate
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.toJson

@Composable
fun SelectStreamingScreen(
    navigate: SelectStreamingNavigate,
    viewModel: SelectStreamingModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.SelectStreaming, tracker = viewModel.analyticsTracker)
    SelectStreamingContent(navigate = navigate, viewModel = viewModel)
}

@Composable
fun SelectStreamingContent(navigate: SelectStreamingNavigate, viewModel: SelectStreamingModel) {
    Scaffold(
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.screen_padding_new)
        ),
        topBar = {
            DisabledSearchToolBar(
                onBackstack = navigate::popBackStack,
                onToSearch = navigate::toSearch
            )
        },
        bottomBar = {
            AdsBanner(R.string.select_streaming_banner, isVisible = viewModel.showAds)
        },
        contentColor = PrimaryBackground
    ) { padding ->
        UiStateResult(
            uiState = viewModel.uiState.collectAsState().value,
            onRefresh = { viewModel.refresh() }
        ) { streaming ->
            Column(modifier = Modifier.padding(padding)) {
                StreamingGrid(streaming = streaming) { stream ->
                    viewModel.saveSelectedStream(stream)
                    navigate.toExploreStreaming()
                }
            }
        }
    }
}

@Composable
fun StreamingGrid(streaming: StreamingData, onClick: (String) -> Unit) {
    val padding = dimensionResource(R.dimen.default_padding)
    LazyVerticalGrid(
        modifier = Modifier.background(PrimaryBackground),
        columns = GridCells.Fixed(count = STREAMING_GRID_COLUMNS),
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        streamingSession(
            top = { SimpleTitle(title = stringResource(R.string.main_steams)) },
            streaming = streaming.selected,
            onClick = onClick
        )
        streamingSession(
            top = { SimpleTitle(title = stringResource(R.string.other_streams)) },
            streaming = streaming.unselected,
            onClick = onClick
        )
    }
}

private fun LazyGridScope.streamingSession(
    top: @Composable () -> Unit,
    streaming: List<StreamingEntity>,
    onClick: (String) -> Unit
) {
    if (streaming.isNotEmpty()) {
        item(span = { GridItemSpan(currentLineSpan = STREAMING_GRID_COLUMNS) }) {
            top()
        }
        items(streaming.size) { index ->
            StreamingItem(streaming = streaming[index], onClick = onClick)
        }
    }
}

@Composable
fun StreamingItem(
    streaming: StreamingEntity,
    onClick: (String) -> Unit
) {
    BasicImage(
        url = streaming.getLogoImage(),
        contentDescription = streaming.name,
        contentScale = ContentScale.FillBounds,
        withBorder = true,
        modifier = Modifier
            .size(dimensionResource(R.dimen.streaming_item_big_size))
            .clickable { onClick.invoke(streaming.toJson()) }
    )
}
