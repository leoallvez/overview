package br.com.deepbyte.overview.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.ui.BasicImage
import br.com.deepbyte.overview.ui.ErrorScreen
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.UiStateResult
import br.com.deepbyte.overview.ui.navigation.events.HomeScreenEvents
import br.com.deepbyte.overview.util.toJson

@Composable
fun NewHomeScreen(
    events: HomeScreenEvents,
    viewModel: NewHomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh() }
    ) { dataResult ->
        if (dataResult == null) {
            ErrorScreen(refresh = { viewModel.refresh() })
        } else {
            StreamingVerticalGrid(
                streamings = dataResult,
                onClick = events::onNavigateToStreamingOverview
            )
        }
    }
}

@Composable
fun StreamingVerticalGrid(
    streamings: List<Streaming>,
    onClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        modifier = Modifier.fillMaxSize()
    ) {
        items(streamings.size) { index ->
            HomeStreamingItem(streaming = streamings[index], onClick = onClick)
        }
    }
}

@Composable
fun HomeStreamingItem(
    streaming: Streaming,
    onClick: (String) -> Unit
) {
    BasicImage(
        url = streaming.getLogoImage(),
        contentDescription = streaming.name,
        withBorder = true,
        modifier = Modifier
            .size(100.dp)
            .padding(dimensionResource(id = R.dimen.default_padding))
            .clickable { onClick.invoke(streaming.toJson()) }
    )
}
