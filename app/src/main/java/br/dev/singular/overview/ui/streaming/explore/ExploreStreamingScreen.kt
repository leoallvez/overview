package br.dev.singular.overview.ui.streaming.explore

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.ExploreStreamingNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch

@Composable
fun ExploreStreamingScreen(
    navigate: ExploreStreamingNavigate,
    viewModel: ExploreStreamingViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.ExploreStreaming, tracker = viewModel.analyticsTracker)

    LaunchedEffect(key1 = Any()) {
        viewModel.loadGenres()
    }

    ExploreStreamingContent(
        navigate = navigate,
        searchFilters = viewModel.searchFilters.collectAsState().value,
        streaming = viewModel.selectedStreaming,
        showAds = viewModel.showAds,
        onRefresh = { viewModel.loadMedias() },
        pagingMediaItems = viewModel.medias.collectAsLazyPagingItems(),
        genres = viewModel.genres.collectAsState().value,
        inFiltering = { newFilters ->
            viewModel.updateData(newFilters)
            viewModel.loadGenres()
        }
    )
}

@Composable
fun ExploreStreamingContent(
    showAds: Boolean,
    searchFilters: SearchFilters,
    streaming: StreamingEntity?,
    onRefresh: () -> Unit,
    genres: List<GenreEntity>,
    navigate: ExploreStreamingNavigate,
    pagingMediaItems: LazyPagingItems<Media>,
    inFiltering: (SearchFilters) -> Unit
) {
    ExploreStreamingBody(
        navigate = navigate,
        showAds = showAds,
        filters = searchFilters,
        streaming = streaming,
        onRefresh = onRefresh,
        inFiltering = inFiltering,
        genres = genres,
        pagingMedias = pagingMediaItems
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExploreStreamingBody(
    showAds: Boolean,
    filters: SearchFilters,
    onRefresh: () -> Unit,
    streaming: StreamingEntity?,
    genres: List<GenreEntity>,
    navigate: ExploreStreamingNavigate,
    pagingMedias: LazyPagingItems<Media>,
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
            FilterBottomSheet(filters, genres, closeFilterBottomSheet, inFiltering)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
            topBar = {
                ExploreStreamingToolBar(onNavigateToSearch = navigate::toSearch)
            },
            bottomBar = {
                AdsBanner(R.string.discover_banner, showAds)
            }
        ) { padding ->
            val filterIsVisible = sheetState.isVisible
            Column(
                Modifier
                    .background(PrimaryBackground)
                    .padding(vertical = 5.dp)
            ) {
                FiltersArea(
                    filters = filters,
                    genres = genres,
                    streaming = streaming,
                    onFilterClick = closeFilterBottomSheet,
                    onStreamingClick = {
                        navigate.toSelectStreaming()
                    }
                )
                when (pagingMedias.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen(showOnTop = filterIsVisible)
                    is LoadState.NotLoading -> {
                        if (pagingMedias.itemCount == 0) {
                            ErrorScreen(showOnTop = filterIsVisible, refresh = onRefresh)
                        } else {
                            MediaPagingVerticalGrid(padding, pagingMedias, navigate::toMediaDetails)
                        }
                    }

                    else -> {
                        NotFoundContentScreen(
                            showOnTop = filterIsVisible,
                            hasFilters = filters.genresIsNotEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiltersArea(
    filters: SearchFilters,
    genres: List<GenreEntity>,
    streaming: StreamingEntity?,
    onStreamingClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.default_padding))
    ) {
        Surface(
            modifier = Modifier
                .clickable { onStreamingClick.invoke() }
                .fillMaxWidth()
                .border(
                    2.dp,
                    Gray.copy(alpha = 0.5f),
                    RoundedCornerShape(dimensionResource(R.dimen.corner))
                )
                .background(SecondaryBackground)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(PrimaryBackground),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StreamingIcon(streaming = streaming, withBorder = false, clickable = false)
                    StreamingScreamTitle(title = streaming?.name)
                }
                Box(Modifier.padding(end = 5.dp)) {
                    Icon(
                        tint = Gray,
                        painter = painterResource(id = R.drawable.baseline_expand_more),
                        contentDescription = stringResource(R.string.streaming)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.default_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TuneIcon()
                Text(
                    modifier = Modifier.width(250.dp),
                    text = filterDescription(filters, genres),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            PulsatingFilterButton(isActivated = filters.areDefaultValues().not()) {
                onFilterClick.invoke()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun PulsatingFilterButton(isActivated: Boolean, onClick: () -> Unit) {
    Pulsating(isPulsing = !isActivated) {
        FilterButton(
            padding = PaddingValues(),
            isActivated = isActivated,
            buttonText = stringResource(R.string.filters),
            complement = {
                Icon(
                    if (isActivated) Icons.Rounded.CheckCircle else Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.filters),
                    modifier = Modifier.size(20.dp),
                    tint = if (isActivated) AccentColor else Gray
                )
            }
        ) {
            onClick.invoke()
        }
    }
}

@Composable
private fun filterDescription(filters: SearchFilters, genres: List<GenreEntity>): String {
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
fun ExploreStreamingToolBar(onNavigateToSearch: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBackground)
            .padding(
                paddingValues = PaddingValues(
                    vertical = dimensionResource(R.dimen.screen_padding)
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val defaultPadding = dimensionResource(R.dimen.default_padding)
            SearchField(
                enabled = false,
                onClick = onNavigateToSearch,
                defaultPaddingValues = PaddingValues(start = defaultPadding, end = defaultPadding),
                placeholder = stringResource(R.string.search_in_all_places)
            )
        }
    }
}

@Composable
fun StreamingScreamTitle(title: String?) {
    title?.let {
        Text(
            text = title,
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
}

@Composable
fun FilterBottomSheet(
    filters: SearchFilters,
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
        FilterMediaType(filters, inFiltering)
        Divider(
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = dimensionResource(id = R.dimen.screen_padding_new)
            ),
            thickness = 1.dp,
            color = Color.Gray
        )
        FilterGenres(genres, filters, inFiltering)
        ClearFilter(filters, inFiltering, Modifier.align(Alignment.End))
    }
}

@Composable
fun ClearFilter(
    filters: SearchFilters,
    inFiltering: (SearchFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    if (filters.areDefaultValues().not()) {
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
                filters.clear()
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
private fun TuneIcon() {
    Icon(
        tint = Color.White,
        modifier = Modifier
            .size(25.dp)
            .padding(start = 0.dp, end = dimensionResource(id = R.dimen.default_padding)),
        painter = painterResource(id = R.drawable.tune),
        contentDescription = stringResource(R.string.filters)
    )
}

@Composable
fun CloseIcon(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.default_padding)),
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
fun FilterMediaType(filters: SearchFilters, onClick: (SearchFilters) -> Unit) {
    val options = MediaTypeEnum.getAllOrdered()
    Column {
        FilterTitle(stringResource(R.string.type))
        Row(modifier = Modifier.fillMaxWidth().background(SecondaryBackground)) {
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
}

@Composable
fun FilterGenres(
    genres: List<GenreEntity>,
    filters: SearchFilters,
    onClick: (SearchFilters) -> Unit
) {
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
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.screen_padding_new)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}
