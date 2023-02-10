package br.com.deepbyte.overview.ui.discover

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.DiscoverParams
import br.com.deepbyte.overview.ui.DiscoverContent
import br.com.deepbyte.overview.ui.ScreenNav
import br.com.deepbyte.overview.ui.TrackScreenView
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents

@Composable
fun ProviderDiscoverScreen(
    params: DiscoverParams,
    events: BasicsMediaEvents,
    viewModel: DiscoverViewModel = hiltViewModel(),
) {
    TrackScreenView(screen = ScreenNav.ProviderDiscover, tracker = viewModel.analyticsTracker)

    val loadData = {
        viewModel.loadDiscoverByProvide(providerId = params.apiId, mediaType = MediaType.TV_SHOW.key)
    }
    var items by remember { mutableStateOf(value = loadData()) }

    DiscoverContent(
        showAds = viewModel.showAds,
        providerName = params.screenTitle,
        pagingItems = items.collectAsLazyPagingItems(),
        onRefresh = { items = loadData() },
        onPopBackStack = { events.onPopBackStack() },
        onToMediaDetails = events::onNavigateToMediaDetails
    )
}
