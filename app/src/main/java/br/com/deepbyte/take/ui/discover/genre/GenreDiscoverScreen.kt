package br.com.deepbyte.take.ui.discover.genre

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.take.Logger
import br.com.deepbyte.take.data.model.DiscoverParams
import br.com.deepbyte.take.ui.DiscoverContent
import br.com.deepbyte.take.ui.ScreenNav
import br.com.deepbyte.take.ui.TrackScreenView
import br.com.deepbyte.take.util.MediaItemClick

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
