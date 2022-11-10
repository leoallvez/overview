package io.github.leoallvez.take.ui.discover.genre

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.data.model.DiscoverParams
import io.github.leoallvez.take.ui.DiscoverContent
import io.github.leoallvez.take.ui.ScreenNav
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun GenreDiscoverScreen(
    logger: Logger,
    params: DiscoverParams,
    onNavigateToMediaDetails: MediaItemClick,
    viewModel: GenreDiscoverViewModel = hiltViewModel(),
) {
    TrackScreenView(screen = ScreenNav.GenreDiscover, logger)

    val loadData = { viewModel.loadDada(genreId = params.apiId, mediaType = params.mediaType) }

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
