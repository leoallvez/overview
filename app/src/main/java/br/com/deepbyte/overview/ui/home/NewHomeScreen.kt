package br.com.deepbyte.overview.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.model.provider.StreamingsWrap
import br.com.deepbyte.overview.ui.AdsBanner
import br.com.deepbyte.overview.ui.BasicImage
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.SearchField
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.UiStateResult
import br.com.deepbyte.overview.ui.navigation.events.HomeScreenEvents
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.util.toJson

@Composable
fun NewHomeScreen(
    events: HomeScreenEvents,
    viewModel: NewHomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)

    HomeContent(events = events, viewModel = viewModel)
}

@Composable
fun HomeContent(
    events: HomeScreenEvents,
    viewModel: NewHomeViewModel
) {
    Scaffold(
        modifier = Modifier
            .background(PrimaryBackground)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
        topBar = {
            Box(
                modifier = Modifier
                    .background(PrimaryBackground)
                    .padding(top = dimensionResource(R.dimen.screen_padding))
            ) {
                SearchField(
                    enabled = false,
                    onClick = {},
                    placeholder = stringResource(R.string.search_in_all_places)
                )
            }
        },
        bottomBar = {
            AdsBanner(R.string.home_banner, isVisible = viewModel.showAds)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(padding)
        ) {
            UiStateResult(
                uiState = viewModel.uiState.collectAsState().value,
                onRefresh = { viewModel.refresh() }
            ) { data ->
                StreamingVerticalGrid(
                    wrap = data,
                    onClick = events::onNavigateToStreamingOverview
                )
            }
        }
    }
}

@Composable
fun StreamingVerticalGrid(
    wrap: StreamingsWrap,
    onClick: (String) -> Unit
) {
    Column {
        Text("Selected", modifier = Modifier.padding(16.dp), color = Color.White)

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 4)
        ) {
            items(wrap.selected.size) { index ->
                HomeStreamingItem(streaming = wrap.selected[index], onClick = onClick)
            }
        }

        Text("Unselected", modifier = Modifier.padding(16.dp), color = Color.White)

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 4)
        ) {
            items(wrap.unselected.size) { index ->
                HomeStreamingItem(streaming = wrap.unselected[index], onClick = onClick)
            }
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
