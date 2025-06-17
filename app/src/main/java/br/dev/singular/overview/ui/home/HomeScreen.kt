package br.dev.singular.overview.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.FilterButton
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaEntityPagingVerticalGrid
import br.dev.singular.overview.ui.MediaTypeFilterButton
import br.dev.singular.overview.ui.NotFoundContentScreen
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.HomeNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.defaultBorder
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigate: HomeNavigate,
    viewModel: HomeViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.Home, tracker = viewModel.analyticsTracker)

    HomeContent(
        navigate = navigate,
        filters = viewModel.searchFilters.collectAsState().value,
        onRefresh = { viewModel.loadMediaPaging() },
        items = viewModel.medias.collectAsLazyPagingItems(),
        genres = viewModel.genres.collectAsState().value,
        inFiltering = viewModel::updateData
    )
}

@Composable
fun HomeContent(
    filters: SearchFilters,
    onRefresh: () -> Unit,
    genres: List<GenreEntity>,
    navigate: HomeNavigate,
    items: LazyPagingItems<MediaEntity>,
    inFiltering: (SearchFilters) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipHalfExpanded = true,
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = TweenSpec(durationMillis = 350, delay = 350),
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
    // TODO: migrate this for material3
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AnimatedVisibility(visible = sheetState.isVisible) {
                FilterBottomSheet(filters, genres, closeFilterBottomSheet, inFiltering)
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            contentColor = PrimaryBackground,
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
            topBar = {
                HomeToolBar(
                    filters = filters,
                    genres = genres,
                    navigate = navigate,
                    openGenreFilter = closeFilterBottomSheet,
                    onSelectMediaType = inFiltering
                )
            }
        ) { padding ->
            when (items.loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.NotLoading -> {
                    if (items.itemCount != 0) {
                        MediaEntityPagingVerticalGrid(padding, items, navigate::toMediaDetails)
                    } else {
                        ErrorScreen(refresh = onRefresh)
                    }
                }
                else -> NotFoundContentScreen(hasFilters = filters.areDefaultValues().not())
            }
        }
    }
}

@Composable
fun HomeToolBar(
    filters: SearchFilters,
    genres: List<GenreEntity>,
    openGenreFilter: () -> Unit,
    onSelectMediaType: (SearchFilters) -> Unit,
    navigate: HomeNavigate
) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.screen_padding))
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SelectStreaming(
                streaming = filters.streaming,
                onClick = { navigate.toSelectStreaming() }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = dimensionResource(R.dimen.default_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterMediaType(filters, genres, onSelectMediaType, openGenreFilter)
        }
    }
}

@Composable
fun SelectStreaming(streaming: StreamingEntity?, onClick: () -> Unit) {
    SelectButton(
        onClick = { onClick.invoke() },
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StreamingIcon(
                    modifier = Modifier.padding(start = 4.dp),
                    size = dimensionResource(id = R.dimen.icon_medium_size),
                    corner = dimensionResource(R.dimen.circle_conner),
                    streaming = streaming,
                    withBorder = false
                )
                HomeScreamTitle(title = streaming?.name ?: String())
            }
        },
        icon = painterResource(id = R.drawable.keyboard_arrow_down)
    )
}

// TODO: this component must be merged with the SelectStreaming component and simplified
@Composable
fun SelectButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    width: Dp? = null,
    height: Dp = dimensionResource(R.dimen.streaming_item_small_size),
    icon: Painter = painterResource(id = R.drawable.baseline_expand_more),
    isActive: Boolean = true
) {
    val color = if (isActive) AccentColor else Gray
    Surface(
        modifier = Modifier
            .height(height)
            .clickable { onClick.invoke() }
            .then(if (width == null) Modifier.fillMaxWidth() else Modifier.width(width))
            .defaultBorder(color = Color.Black, corner = R.dimen.circle_conner)
            .background(SecondaryBackground)
    ) {
        Row(
            Modifier.background(PrimaryBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content.invoke()
            Box(
                Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.default_padding)
                    )
                    .clip(CircleShape)
                    .background(SecondaryBackground)
            ) {
                Icon(
                    tint = color,
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.icon_medium_size))
                )
            }
        }
    }
}

@Composable
private fun genreDescription(genreId: Long?, genres: List<GenreEntity>): String {
    val genre = genres.firstOrNull { it.apiId == genreId }
    return genre?.nameTranslation() ?: String()
}

@Composable
fun HomeScreamTitle(title: String?) {
    title?.let {
        Text(
            text = title,
            color = AccentColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.screen_padding),
                    vertical = dimensionResource(R.dimen.default_padding)
                )
                .widthIn(max = 170.dp),
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
            .wrapContentHeight()
            .background(SecondaryBackground)
            .padding(dimensionResource(R.dimen.screen_padding_new))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterTitle(stringResource(R.string.filter_by_genre))
            CloseIcon(onClick = closeAction)
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.screen_padding_new)))
        FilterGenres(genres, filters) {
            if (filters.genreId == it.genreId) {
                inFiltering.invoke(filters.copy(genreId = null))
            } else {
                inFiltering.invoke(it)
                closeAction.invoke()
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.screen_padding_new)))
    }
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(25.dp)
            .clickable { onClick.invoke() }
    ) {
        Icon(
            tint = Color.White,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center),
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(R.string.close)
        )
    }
}

@Composable
fun FilterMediaType(
    filters: SearchFilters,
    genres: List<GenreEntity>,
    onSelectMedia: (SearchFilters) -> Unit,
    onOpenGenreFilter: () -> Unit
) {
    val onClearFilter = {
        onSelectMedia(filters.copy(mediaType = MediaType.ALL, genreId = null))
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().background(PrimaryBackground)
        ) {
            when (filters.mediaType.key) {
                MediaType.ALL.key -> {
                    val options = MediaType.getAllOrdered()
                    options.forEach { type ->
                        MediaTypeFilterButton(type, filters.mediaType.key) {
                            with(filters) {
                                if (mediaType != type) {
                                    onSelectMedia(
                                        filters.copy(mediaType = type, genreId = null)
                                    )
                                }
                            }
                        }
                    }
                }
                MediaType.TV_SHOW.key -> {
                    ClosableFilterButton(
                        buttonText = stringResource(R.string.tv_show),
                        isActivated = true,
                        onClick = onClearFilter
                    )
                    SelectGenreButton(filters, genres, onOpenGenreFilter)
                }
                MediaType.MOVIE.key -> {
                    ClosableFilterButton(
                        buttonText = stringResource(R.string.movies),
                        isActivated = true,
                        onClick = onClearFilter
                    )
                    SelectGenreButton(filters, genres, onOpenGenreFilter)
                }
            }
        }
    }
}

@Composable
fun SelectGenreButton(filters: SearchFilters, genres: List<GenreEntity>, onClick: () -> Unit) {
    val isActivated = filters.genreId != null
    FilterButton(
        onClick = onClick,
        isActivated = isActivated,
        backgroundColor = SecondaryBackground,
        buttonText = if (isActivated) {
            genreDescription(filters.genreId, genres)
        } else {
            stringResource(R.string.genre)
        },
        complement = {
            Icon(
                tint = if (isActivated) AccentColor else Gray,
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.baseline_expand_more),
                contentDescription = stringResource(R.string.filters)
            )
        }
    )
}

@Composable
fun FilterGenres(
    genres: List<GenreEntity>,
    filters: SearchFilters,
    onClick: (SearchFilters) -> Unit
) {
    Column {
        FlowRow(
            crossAxisSpacing = dimensionResource(R.dimen.screen_padding),
            modifier = Modifier.fillMaxWidth(),
            mainAxisAlignment = MainAxisAlignment.Start
        ) {
            genres.forEach { genre ->
                ClosableFilterButton(
                    buttonText = genre.nameTranslation(),
                    isActivated = filters.genreId == genre.apiId
                ) {
                    onClick.invoke(filters.copy(genreId = genre.apiId))
                }
            }
        }
    }
}

@Composable
fun ClosableFilterButton(
    buttonText: String,
    isActivated: Boolean,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp),
    onClick: () -> Unit
) {
    FilterButton(
        buttonText = buttonText,
        backgroundColor = SecondaryBackground,
        isActivated = isActivated,
        contentPadding = contentPadding,
        complement = {
            if (isActivated) {
                Icon(
                    tint = AccentColor,
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.close)
                )
            }
        }
    ) {
        onClick.invoke()
    }
}

@Composable
fun FilterTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}
