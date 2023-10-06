package br.dev.singular.overview.ui.streaming

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import br.dev.singular.overview.R
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.FilterButton
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeFilterButton
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.Pulsating
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SearchField
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.ToolbarButton
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.StreamingExploreNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch

@Composable
fun StreamingExploreScreen(
    streaming: StreamingEntity,
    navigate: StreamingExploreNavigate,
    viewModel: StreamingExploreViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.StreamingExplore, tracker = viewModel.analyticsTracker)

    viewModel.setStreamingId(streaming.apiId)

    LaunchedEffect(key1 = Any()) {
        viewModel.loadGenres()
    }

    StreamingExploreContent(
        navigate = navigate,
        searchFilters = viewModel.searchFilters.collectAsState().value,
        streaming = streaming,
        showAds = viewModel.showAds,
        onRefresh = { viewModel.reloadMedias() },
        pagingMediaItems = viewModel.medias.collectAsLazyPagingItems(),
        genresItems = viewModel.genres.collectAsState().value,
        inFiltering = { newFilters ->
            viewModel.updateData(newFilters)
            viewModel.loadGenres()
        }
    )
}

@Composable
fun StreamingExploreContent(
    showAds: Boolean,
    searchFilters: SearchFilters,
    streaming: StreamingEntity,
    onRefresh: () -> Unit,
    genresItems: List<GenreEntity>,
    navigate: StreamingExploreNavigate,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (SearchFilters) -> Unit
) {
    StreamingExploreBody(
        navigate = navigate,
        showAds = showAds,
        searchFilters = searchFilters,
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
    searchFilters: SearchFilters,
    onRefresh: () -> Unit,
    streaming: StreamingEntity,
    genresItems: List<GenreEntity>,
    navigate: StreamingExploreNavigate,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (SearchFilters) -> Unit
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
            FilterBottomSheet(searchFilters, genresItems, closeFilterBottomSheet, inFiltering)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
            topBar = {
                StreamingToolBar(
                    backButtonAction = navigate::popBackStack,
                    onNavigateToSearch = navigate::toSearch
                )
            },
            bottomBar = {
                AdsBanner(R.string.discover_banner, showAds)
            }
        ) { padding ->
            val filterIsVisible = sheetState.isVisible
            Column(Modifier.background(PrimaryBackground)) {
                FiltersArea(
                    searchFilters = searchFilters,
                    genres = genresItems,
                    streaming = streaming,
                    closeFilterBottomSheet
                )
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
                                navigate::toMediaDetails
                            )
                        }
                    }
                    else -> {
                        NotFoundContentScreen(
                            showOnTop = filterIsVisible,
                            hasFilters = searchFilters.genresIsIsNotEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiltersArea(
    searchFilters: SearchFilters,
    genres: List<GenreEntity>,
    streaming: StreamingEntity,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.default_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StreamingIcon(streaming = streaming, withBorder = false)
                StreamingScreamTitle(streamingName = streaming.name)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.default_padding),
                    vertical = dimensionResource(R.dimen.screen_padding_new)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TuneIcon()
                Text(
                    text = filterDescription(searchFilters, genres),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Pulsating(active = searchFilters.areDefaultValues()) {
                FilterButton(
                    padding = PaddingValues(),
                    isActivated = searchFilters.areDefaultValues().not(),
                    buttonText = stringResource(R.string.filters)
                ) {
                    onClick.invoke()
                }
            }
        }
    }
}

@Composable
private fun filterDescription(searchFilters: SearchFilters, genres: List<GenreEntity>): String {
    val mediaDescription = mediaTypeDescription(searchFilters.mediaType)
    val genresDescription = genresDescription(searchFilters.genresIds, genres)

    return "$mediaDescription $genresDescription"
}

@Composable
private fun mediaTypeDescription(mediaType: MediaTypeEnum): String = when (mediaType) {
    MediaTypeEnum.ALL -> stringResource(id = R.string.all)
    MediaTypeEnum.TV_SHOW -> stringResource(id = R.string.tv_show)
    else -> stringResource(id = R.string.movies)
}

@Composable
private fun genresDescription(genresSelectedIds: List<Long>, genres: List<GenreEntity>): String {
    return if (genresSelectedIds.isNotEmpty()) {
        var result = ""
        val filtered = genres.filter { it.apiId in genresSelectedIds }

        for ((index: Int, genre: GenreEntity) in filtered.withIndex()) {
            val genreName: String = genre.nameTranslation()

            result += if (filtered.lastIndex == index) {
                genreName
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
                    horizontal = 5.dp
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
    searchFilters: SearchFilters,
    genres: List<GenreEntity>,
    closeAction: () -> Unit,
    inFiltering: (SearchFilters) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(SecondaryBackground)
            .padding(
                vertical = dimensionResource(R.dimen.default_padding),
                horizontal = dimensionResource(R.dimen.screen_padding_new)
            )
    ) {
        CloseIcon(closeAction)
        FilterMediaType(searchFilters, inFiltering)
        Divider(
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = dimensionResource(id = R.dimen.screen_padding_new)
            ),
            thickness = 1.dp,
            color = Color.Gray
        )
        FilterGenres(genres, searchFilters, inFiltering)
        ClearFilter(searchFilters, inFiltering, Modifier.align(Alignment.End))
    }
}

@Composable
fun ClearFilter(
    searchFilters: SearchFilters,
    inFiltering: (SearchFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    if (searchFilters.areDefaultValues().not()) {
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
                searchFilters.clear()
                inFiltering.invoke(searchFilters)
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
private fun TuneIcon() {
    Icon(
        tint = Color.White,
        modifier = Modifier
            .size(25.dp)
            .padding(end = 5.dp),
        painter = painterResource(id = R.drawable.tune),
        contentDescription = stringResource(R.string.filters)
    )
}

@Composable
fun CloseIcon(onClick: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().padding(top = 5.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .height(22.dp)
                .background(Gray.copy(alpha = 0.5f))
                .clickable { onClick.invoke() }
        ) {
            Icon(
                tint = SecondaryBackground,
                modifier = Modifier.size(22.dp),
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.close)
            )
        }
    }
}

@Composable
fun FilterMediaType(searchFilters: SearchFilters, onClick: (SearchFilters) -> Unit) {
    val options = MediaTypeEnum.getAllOrdered()
    Column {
        FilterTitle(stringResource(R.string.type))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryBackground)
        ) {
            options.forEach { type ->
                MediaTypeFilterButton(type, searchFilters.mediaType.key) {
                    with(searchFilters) {
                        if (mediaType != type) {
                            mediaType = type
                            clearGenresIds()
                            onClick.invoke(searchFilters)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterGenres(genres: List<GenreEntity>, searchFilters: SearchFilters, onClick: (SearchFilters) -> Unit) {
    Column() {
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
                    isActivated = searchFilters.hasGenreWithId(genre.apiId)
                ) {
                    searchFilters.updateGenreIds(genre.apiId)
                    onClick.invoke(searchFilters)
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
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.screen_padding_new)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}
