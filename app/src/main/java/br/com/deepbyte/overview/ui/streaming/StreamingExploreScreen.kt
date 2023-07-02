package br.com.deepbyte.overview.ui.streaming

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.filters.Filters
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.StreamingEvents
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.AlertColor
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch

@Composable
fun StreamingExploreScreen(
    streaming: Streaming,
    events: StreamingEvents,
    viewModel: StreamingExploreViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.StreamingExplore, tracker = viewModel.analyticsTracker)

    val loadData = {
        viewModel.loadGenres()
        viewModel.getMediasPaging(streaming.apiId)
    }

    val filters = viewModel.filters.collectAsState().value
    var mediaItems by remember { mutableStateOf(value = loadData()) }
    val setMediaItems = { mediaItems = loadData() }

    StreamingExploreContent(
        events = events,
        filters = filters,
        streaming = streaming,
        showAds = viewModel.showAds,
        onRefresh = setMediaItems,
        pagingMediaItems = mediaItems.collectAsLazyPagingItems(),
        genresItems = viewModel.genres.collectAsState().value,
        inFiltering = {
            viewModel.updateFilters(it)
            setMediaItems()
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
    events: StreamingEvents,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (Filters) -> Unit
) {
    StreamingExploreBody(
        events = events,
        showAds = showAds,
        filters = filters,
        streaming = streaming,
        onRefresh = onRefresh,
        inFiltering = inFiltering,
        genresItems = genresItems,
        pagingMediaItems = pagingMediaItems
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StreamingExploreBody(
    showAds: Boolean,
    filters: Filters,
    onRefresh: () -> Unit,
    streaming: Streaming,
    genresItems: List<Genre>,
    events: StreamingEvents,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (Filters) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipHalfExpanded = true,
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = TweenSpec(durationMillis = 300, delay = 10),
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    val closeFilterBottomSheet = {
        coroutineScope.launch {
            if (sheetState.isVisible) {
                sheetState.hide()
            } else {
                sheetState.show()
            }
        }
        Unit
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            FilterBottomSheet(filters, genresItems, closeFilterBottomSheet, inFiltering)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
            topBar = {
                StreamingToolBar(
                    backButtonAction = events::onPopBackStack,
                    onNavigateToSearch = events::onNavigateToSearch
                )
            },
            bottomBar = {
                AdsBanner(R.string.discover_banner, showAds)
            }
        ) { padding ->
            val filterIsVisible = sheetState.isVisible
            Column(Modifier.background(PrimaryBackground)) {
                FiltersArea(
                    filters = filters,
                    genres = genresItems,
                    streaming = streaming,
                    closeFilterBottomSheet
                )
                VerticalSpacer(dimensionResource(R.dimen.default_padding))
                when (pagingMediaItems.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen(showOnTop = filterIsVisible)
                    is LoadState.NotLoading -> {
                        if (pagingMediaItems.itemCount == 0) {
                            ErrorScreen(
                                showOnTop = filterIsVisible,
                                refresh = onRefresh
                            )
                        } else {
                            MediaPagingVerticalGrid(
                                padding,
                                pagingMediaItems,
                                events::onNavigateToMediaDetails
                            )
                        }
                    }
                    else -> {
                        NotFoundContentScreen(
                            showOnTop = filterIsVisible,
                            hasFilters = filters.genresIsIsNotEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiltersArea(
    filters: Filters,
    genres: List<Genre>,
    streaming: Streaming,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.default_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StreamingIcon(streaming = streaming, withBorder = true)
                StreamingScreamTitle(streamingName = streaming.name)
            }

            FilterButton(
                padding = PaddingValues(),
                isActivated = filters.genresIsIsNotEmpty(),
                buttonText = stringResource(R.string.filters),
                complement = {
                    Text(
                        text = filters.genreQuantity(),
                        modifier = Modifier.padding(1.dp),
                        color = Color.White
                    )
                }
            ) {
                onClick.invoke()
            }
        }
        Text(
            text = filterDescription(filters, genres),
            color = AccentColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.default_padding),
                vertical = dimensionResource(R.dimen.screen_padding)
            )
        )
    }
}

@Composable
private fun filterDescription(filters: Filters, genres: List<Genre>): String {
    val mediaDescription = mediaTypeDescription(filters.mediaType)
    val genresDescription = genresDescription(filters.genresIds, genres)

    return "$mediaDescription $genresDescription"
}

@Composable
private fun mediaTypeDescription(mediaType: MediaTypeEnum): String = when (mediaType) {
    MediaTypeEnum.ALL -> stringResource(id = R.string.all)
    MediaTypeEnum.TV_SHOW -> stringResource(id = R.string.tv_show)
    else -> stringResource(id = R.string.movies)
}

@Composable
private fun genresDescription(genresSelectedIds: List<Long>, genres: List<Genre>): String {
    return if (genresSelectedIds.isNotEmpty()) {
        var result = ""
        val filtered = genres.filter { it.apiId in genresSelectedIds }

        for ((index: Int, genre: Genre) in filtered.withIndex()) {
            val genreName: String = genre.nameTranslation()

            result += if (filtered.lastIndex == index) {
                "$genreName."
            } else {
                if (index == filtered.lastIndex - 1) {
                    "$genreName & "
                } else {
                    "$genreName${if (filtered.size > 1) ", " else ""}"
                }
            }
        }
        ": ${result.lowercase().replaceFirstChar { it.uppercase() }}"
    } else {
        ""
    }
}

@Composable
fun StreamingToolBar(
    backButtonAction: () -> Unit,
    onNavigateToSearch: () -> Unit
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
            ) { backButtonAction.invoke() }
        }
        SearchField(
            enabled = false,
            onClick = onNavigateToSearch,
            placeholder = stringResource(R.string.search_in_all_places)
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
fun FilterBottomSheet(
    filters: Filters,
    genres: List<Genre>,
    closeAction: () -> Unit,
    inFiltering: (Filters) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .background(SecondaryBackground)
            .padding(
                vertical = dimensionResource(R.dimen.default_padding),
                horizontal = 15.dp
            )
    ) {
        CloseIcon(closeAction)
        FilterMediaType(filters, inFiltering)
        FilterGenres(genres, filters, inFiltering)
        ClearFilterGenres(filters, inFiltering, Modifier.align(Alignment.End))
    }
}

@Composable
fun ClearFilterGenres(
    filters: Filters,
    inFiltering: (Filters) -> Unit,
    modifier: Modifier = Modifier
) {
    if (filters.genresIsIsNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(bottom = dimensionResource(R.dimen.screen_padding)),
            verticalArrangement = Arrangement.Bottom
        ) {
            FilterButton(
                isActivated = true,
                colorActivated = AlertColor,
                backgroundColor = SecondaryBackground,
                buttonText = stringResource(R.string.clear_filters),
                complement = {
                    CleanFilterIcon()
                }

            ) {
                filters.clearGenresIds()
                inFiltering.invoke(filters)
            }
        }
    }
}

@Composable
private fun CleanFilterIcon() {
    Icon(
        tint = AlertColor,
        modifier = Modifier
            .size(20.dp)
            .padding(1.dp),
        painter = painterResource(id = R.drawable.delete_outline),
        contentDescription = stringResource(R.string.clear_filters)
    )
}

@Composable
fun CloseIcon(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(25.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Icon(
            tint = Color.White,
            modifier = Modifier
                .size(25.dp)
                .clickable { onClick.invoke() },
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(R.string.close)
        )
    }
}

@Composable
fun FilterMediaType(filters: Filters, onClick: (Filters) -> Unit) {
    val options = MediaTypeEnum.getAllOrdered()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondaryBackground)
    ) {
        options.forEach { type ->
            MediaTypeFilterButton(type, filters.mediaType.key) {
                with(filters) {
                    if (mediaType != type) {
                        mediaType = type
                        clearGenresIds()
                        onClick.invoke(filters)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterGenres(genres: List<Genre>, filters: Filters, onClick: (Filters) -> Unit) {
    Column {
        FilterTitle(stringResource(R.string.genres))
        FlowRow(
            crossAxisSpacing = dimensionResource(R.dimen.screen_padding),
            modifier = Modifier.fillMaxWidth(),
            mainAxisAlignment = MainAxisAlignment.Start
        ) {
            genres.forEach { genre ->
                FilterButton(
                    buttonText = genre.nameTranslation(),
                    backgroundColor = SecondaryBackground,
                    isActivated = filters.hasGenreWithId(genre.apiId)
                ) {
                    filters.updateGenreIds(genre.apiId)
                    onClick.invoke(filters)
                }
            }
        }
    }
}

@Composable
fun FilterTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier.padding(vertical = 10.dp),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}
