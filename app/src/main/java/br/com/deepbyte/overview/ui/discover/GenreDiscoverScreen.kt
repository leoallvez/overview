package br.com.deepbyte.overview.ui.discover

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.overview.data.model.DiscoverParams
import br.com.deepbyte.overview.ui.DiscoverContent
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents

@Composable
fun GenreDiscoverScreen(
    params: DiscoverParams,
    events: BasicsMediaEvents,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.GenreDiscover, viewModel.analyticsTracker)

    val loadData = {
        viewModel.loadDiscoverByGenre(genreId = params.apiId, mediaType = params.mediaType)
    }

    var items by remember { mutableStateOf(value = loadData()) }

    DiscoverContent(
        showAds = viewModel.showAds,
        providerName = params.screenTitle,
        pagingItems = items.collectAsLazyPagingItems(),
        onRefresh = { items = loadData() },
        onPopBackStack = { events.onPopBackStack() },
        onToMediaDetails = events::onNavigateToMediaDetails,
    )
}
