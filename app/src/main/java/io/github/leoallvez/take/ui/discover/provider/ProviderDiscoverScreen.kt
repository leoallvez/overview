package io.github.leoallvez.take.ui.discover.provider

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.data.MediaType
import io.github.leoallvez.take.data.model.DiscoverParams
import io.github.leoallvez.take.ui.DiscoverContent
import io.github.leoallvez.take.ui.ScreenNav
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun ProviderDiscoverScreen(
    logger: Logger,
    params: DiscoverParams,
    onNavigateToMediaDetails: MediaItemClick,
    viewModel: ProviderDiscoverViewModel = hiltViewModel(),
) {
    TrackScreenView(screen = ScreenNav.ProviderDiscover, logger)

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
