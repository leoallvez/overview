package br.com.deepbyte.overview.ui.streaming

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.Filters
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.BasicsMediaEvents
import br.com.deepbyte.overview.ui.search.SearchIcon
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun StreamingExploreScreen(
    streaming: Streaming,
    events: BasicsMediaEvents,
    viewModel: StreamingExploreViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.StreamingExplore, tracker = viewModel.analyticsTracker)

    val loadData = { type: MediaTypeEnum ->
        viewModel.loadGenresByMediaType(type)
        viewModel.getMediasPaging(type, streaming.apiId)
    }

    var items by remember { mutableStateOf(value = loadData(MediaTypeEnum.MOVIE)) }

    var filters by rememberSaveable { mutableStateOf(Filters()) }

    StreamingExploreContent(
        events = events,
        filters = filters,
        streaming = streaming,
        showAds = viewModel.showAds,
        onRefresh = { items = loadData(MediaTypeEnum.ALL) },
        pagingMediaItems = items.collectAsLazyPagingItems(),
        genresItems = viewModel.genres.collectAsState().value,
        inFiltering = { _filters ->
            filters = _filters
            items = loadData(filters.mediaType)
        }
    )
}

@Composable
fun StreamingExploreContent(
    showAds: Boolean,
    filters: Filters,
    streaming: Streaming,
    onRefresh: () -> Unit,
    genresItems: List<Genre>,
    events: BasicsMediaEvents,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (Filters) -> Unit
) {
    when (pagingMediaItems.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
        is LoadState.NotLoading -> {
            ScreenResult(
                events = events,
                showAds = showAds,
                filters = filters,
                streaming = streaming,
                genresItems = genresItems,
                pagingMediaItems = pagingMediaItems,
                inFiltering = inFiltering
            )
        } else -> ErrorScreen(onRefresh)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenResult(
    showAds: Boolean,
    streaming: Streaming,
    filters: Filters,
    genresItems: List<Genre>,
    events: BasicsMediaEvents,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (Filters) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { SelectGenresBottomSheet(filters, genresItems, inFiltering) },
        modifier = Modifier.fillMaxSize()
    ) {
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
            if (pagingMediaItems.itemCount == 0) {
                ErrorOnLoading()
            } else {
                Column(Modifier.background(PrimaryBackground)) {
                    FiltersArea(filters, onClick = {
                        coroutineScope.launch {
                            if (sheetState.isVisible) {
                                sheetState.hide()
                            } else {
                                sheetState.show()
                            }
                        }
                    })
                    VerticalSpacer(dimensionResource(R.dimen.screen_padding))
                    MediaPagingVerticalGrid(
                        padding,
                        pagingMediaItems,
                        events::onNavigateToMediaDetails
                    )
                }
            }
        }
    }
}

@Composable
fun FiltersArea(
    filters: Filters,
    onClick: () -> Job
) {
    Row(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.default_padding))) {
        FilterButton(
            onClick = { onClick.invoke() },
            isActivated = filters.genresIsEmpty(),
            buttonText = stringResource(R.string.genres)
        )
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

@Composable
fun SelectGenresBottomSheet(
    filters: Filters,
    genres: List<Genre>,
    inFiltering: (Filters) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(dimensionResource(R.dimen.screen_padding))
    ) {
        val options = MediaTypeEnum.getAllOrdered()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.screen_padding))
        ) {
            options.forEach { mediaType ->
                MediaTypeFilterButton(mediaType, filters.mediaType.key) {
                    filters.mediaType = mediaType
                    inFiltering.invoke(filters)
                }
            }
        }
        Text(
            text = "Size ${genres.size}",
            style = MaterialTheme.typography.h6,
            color = Color.White,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.screen_padding))
        )

        FlowRow(
            crossAxisSpacing = dimensionResource(R.dimen.screen_padding),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.screen_padding)),
            mainAxisAlignment = MainAxisAlignment.Start
        ) {
            genres.forEach { genre ->
                val translatedName = getGenreTranslation.invoke(genre.apiId)
                FilterButton(
                    onClick = {},
                    isActivated = false,
                    buttonText = translatedName ?: genre.name
                )
            }
        }
    }
}
