package br.com.deepbyte.take.ui.discover.provider

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.take.data.MediaType
import br.com.deepbyte.take.data.model.DiscoverParams
import br.com.deepbyte.take.ui.DiscoverContent
import br.com.deepbyte.take.ui.ScreenNav
import br.com.deepbyte.take.ui.TrackScreenView
import br.com.deepbyte.take.util.MediaItemClick

@Composable
fun ProviderDiscoverScreen(
    params: DiscoverParams,
    onNavigateToMediaDetails: MediaItemClick,
    viewModel: ProviderDiscoverViewModel = hiltViewModel(),
) {
    TrackScreenView(screen = ScreenNav.ProviderDiscover, tracker = viewModel.analyticsTracker)

    val loadData = { viewModel.loadDada(providerId = params.apiId, mediaType = MediaType.TV.key) }
    var items by remember { mutableStateOf(value = loadData()) }

    DiscoverContent(
        providerName = params.screenTitle,
        pagingItems = items.collectAsLazyPagingItems(),
        onRefresh = { items = loadData() },
        onPopBackStack = {
            onNavigateToMediaDetails.invoke(params.mediaId, params.mediaType)
        },
        onToMediaDetails = onNavigateToMediaDetails
    )
}
