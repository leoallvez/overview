package br.dev.singular.overview.ui.streaming.explore

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.ui.AdsBanner
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.FilterButton
import br.dev.singular.overview.ui.IconButton
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaEntityPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeFilterButton
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.ExploreStreamingNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.defaultBorder
import br.dev.singular.overview.util.isNull
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch

@Composable
fun ExploreStreamingScreen(
    navigate: ExploreStreamingNavigate,
    viewModel: ExploreStreamingViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.ExploreStreaming, tracker = viewModel.analyticsTracker)

    LaunchedEffect(true) {
        viewModel.loadGenres()
    }

    ExploreStreamingContent(
        navigate = navigate,
        filters = viewModel.searchFilters.collectAsState().value,
        showAds = viewModel.showAds,
        onRefresh = { viewModel.loadMediaPaging() },
        items = viewModel.medias.collectAsLazyPagingItems(),
        genres = viewModel.genres.collectAsState().value,
        inFiltering = { newFilters ->
            viewModel.updateData(newFilters)
            viewModel.loadGenres()
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExploreStreamingContent(
    showAds: Boolean,
    filters: SearchFilters,
    onRefresh: () -> Unit,
    genres: List<GenreEntity>,
    navigate: ExploreStreamingNavigate,
    items: LazyPagingItems<MediaEntity>,
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
                ExploreStreamingToolBar(
                    filters = filters,
                    genres = genres,
                    navigate = navigate,
                    onFilter = closeFilterBottomSheet
                )
            },
            bottomBar = {
                AdsBanner(R.string.discover_banner, showAds)
            }
        ) { padding ->
            val filterIsVisible = sheetState.isVisible
            when (items.loadState.refresh) {
                is LoadState.Loading -> LoadingScreen(showOnTop = filterIsVisible)
                is LoadState.NotLoading -> {
                    if (items.itemCount == 0) {
                        ErrorScreen(showOnTop = filterIsVisible, refresh = onRefresh)
                    } else {
                        MediaEntityPagingVerticalGrid(padding, items, navigate::toMediaDetails)
                    }
                }
                else -> {
                    NotFoundContentScreen(
                        showOnTop = filterIsVisible,
                        hasFilters = filters.genreId.isNull()
                    )
                }
            }
        }
    }
}

@Composable
fun ExploreStreamingToolBar(
    filters: SearchFilters,
    genres: List<GenreEntity>,
    onFilter: () -> Unit,
    navigate: ExploreStreamingNavigate
) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.default_padding))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = dimensionResource(R.dimen.default_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.streaming_item_small_size))
                    .clickable { navigate.toSelectStreaming() }
                    .width(255.dp)
                    .defaultBorder(color = AccentColor, corner = R.dimen.circle_conner)
                    .background(SecondaryBackground)
            ) {
                Row(
                    Modifier.background(PrimaryBackground),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StreamingIcon(
                            modifier = Modifier.padding(start = 4.dp),
                            size = 30.dp,
                            corner = dimensionResource(R.dimen.circle_conner),
                            streaming = filters.streaming,
                            withBorder = false,
                            clickable = false
                        )
                        StreamingScreamTitle(title = filters.streaming?.name)
                    }
                    Box(Modifier.padding(horizontal = dimensionResource(R.dimen.default_padding))) {
                        Icon(
                            tint = AccentColor,
                            painter = painterResource(id = R.drawable.baseline_expand_more),
                            contentDescription = stringResource(R.string.streaming)
                        )
                    }
                }
            }
            ToolBarIcon(
                painter = Icons.Default.Search,
                description = R.string.search_icon
            ) { navigate.toSearch() }
            ToolBarIcon(
                painter = Icons.Default.FavoriteBorder,
                description = R.string.like_button
            ) { navigate.toLiked() }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.screen_padding_new)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterButton(
                text = filterDescription(filters, genres),
                isActivated = filters.areDefaultValues().not()
            ) {
                onFilter.invoke()
            }
        }
    }
}

@Composable
fun ToolBarIcon(painter: ImageVector, @StringRes description: Int, onClick: () -> Unit) {
    IconButton(
        painter = painter,
        descriptionResource = description,
        background = Color.White.copy(alpha = 0.1f),
        padding = PaddingValues(),
        onClick = onClick
    )
}

@Composable
fun FilterButton(text: String, isActivated: Boolean, onClick: () -> Unit) {
    val color = if (isActivated) AccentColor else Gray
    OutlinedButton(
        onClick = { onClick.invoke() },
        shape = RoundedCornerShape(percent = 75),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.default_padding)
        ),
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth(),
        border = BorderStroke(dimensionResource(R.dimen.border_width), color),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = PrimaryBackground
        )
    ) {
        Icon(
            painterResource(id = R.drawable.tune),
            contentDescription = stringResource(id = R.string.filters),
            modifier = Modifier.size(20.dp),
            tint = color
        )
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.caption,
            fontWeight = if (isActivated) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(dimensionResource(R.dimen.default_padding))
        )
    }
}

@Composable
private fun filterDescription(
    filters: SearchFilters,
    genres: List<GenreEntity>
): String {
    val media = mediaTypeDescription(filters.mediaType)
    val genre = genreDescription(filters.genreId, genres)
    return "$media $genre"
}

@Composable
private fun mediaTypeDescription(mediaType: MediaType): String = when (mediaType) {
    MediaType.ALL -> stringResource(id = R.string.all)
    MediaType.TV_SHOW -> stringResource(id = R.string.tv_show)
    else -> stringResource(id = R.string.movies)
}

@Composable
private fun genreDescription(genreId: Long?, genres: List<GenreEntity>): String {
    val genre = genres.firstOrNull { it.apiId == genreId }
    return if (genre != null) "• ${genre.nameTranslation()}" else ""
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
            color = Gray
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
                inFiltering.invoke(SearchFilters(streaming = filters.streaming))
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
            .padding(top = dimensionResource(id = R.dimen.default_padding)),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .height(20.dp)
                .background(Gray.copy(alpha = 0.5f))
                .clickable { onClick.invoke() }
        ) {
            Icon(
                tint = SecondaryBackground,
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.close)
            )
        }
    }
}

@Composable
fun FilterMediaType(filters: SearchFilters, onClick: (SearchFilters) -> Unit) {
    val options = MediaType.getAllOrdered()
    Column {
        FilterTitle(stringResource(R.string.type))
        Row(modifier = Modifier.fillMaxWidth().background(SecondaryBackground)) {
            options.forEach { type ->
                MediaTypeFilterButton(type, filters.mediaType.key) {
                    with(filters) {
                        if (mediaType != type) {
                            onClick.invoke(filters.copy(mediaType = type, genreId = null))
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
                    isActivated = filters.genreId == genre.apiId
                ) {
                    onClick.invoke(filters.copy(genreId = genre.apiId))
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
