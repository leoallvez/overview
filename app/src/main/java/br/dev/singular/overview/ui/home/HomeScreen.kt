package br.dev.singular.overview.ui.home

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagHome
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.FilterButton
import br.dev.singular.overview.ui.LoadingScreen
import br.dev.singular.overview.ui.MediaTypeFilterButton
import br.dev.singular.overview.ui.NothingFoundScreen
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.TagScreenView
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.HomeNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.DarkGray
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.defaultBorder
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch

private fun tagClick(detail: String, id: Long = 0L) {
    TagManager.logClick(TagHome.PATH, detail, id)
}

@Composable
fun HomeScreen(
    navigate: HomeNavigate,
    viewModel: HomeViewModel = hiltViewModel()
) {
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
    items: LazyPagingItems<MediaUiModel>,
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
                GenreFilterBottomSheet(filters, genres, closeFilterBottomSheet, inFiltering)
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            contentColor = PrimaryBackground,
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(horizontal = dimensionResource(R.dimen.spacing_small)),
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
                is LoadState.Loading -> LoadingScreen(tagPath = TagHome.PATH)
                is LoadState.NotLoading -> {
                    if (items.itemCount == 0) {
                        ErrorScreen(tagPath = TagHome.PATH, refresh = onRefresh)
                    } else {
                        TagScreenView(TagHome.PATH, TagStatus.SUCCESS)
                        UiMediaGrid(
                            items = items,
                            modifier = Modifier
                                .background(PrimaryBackground)
                                .padding(top = padding.calculateTopPadding())
                                .fillMaxSize(),
                            onClick = {
                                TagMediaManager.logClick(TagHome.PATH, it.id)
                                navigate.toMediaDetails(it)
                            }
                        )
                    }
                }
                else -> {
                    NothingFoundScreen(
                        tagPath = TagHome.PATH,
                        hasFilters = filters.areDefaultValues().not()
                    )
                }
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
            .padding(bottom = dimensionResource(R.dimen.spacing_small))
    ) {
        Row(
            modifier = Modifier.height(50.dp).padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SelectStreaming(
                streaming = filters.streaming,
                onClick = {
                    tagClick(TagCommon.Detail.SELECT_STREAMING)
                    navigate.toSelectStreaming()
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = dimensionResource(R.dimen.spacing_extra_small)),
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
                    size = 30.dp,
                    corner = dimensionResource(R.dimen.circle_conner),
                    streaming = streaming,
                    withBorder = false
                )
                HomeScreamTitle(title = streaming?.name ?: String())
            }
        },
        icon = painterResource(id = R.drawable.ic_arrow_down)
    )
}

// TODO: this component must be merged with the SelectStreaming component and simplified
@Composable
fun SelectButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    width: Dp? = null,
    height: Dp = 40.dp,
    icon: Painter = painterResource(id = R.drawable.ic_arrow_down),
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
                        horizontal = dimensionResource(R.dimen.spacing_extra_small)
                    )
                    .clip(CircleShape)
                    .background(SecondaryBackground)
                    .border(2.dp, DarkGray, CircleShape)
            ) {
                Icon(
                    tint = color,
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.spacing_extra_extra_large))
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
                    horizontal = dimensionResource(R.dimen.spacing_small),
                    vertical = dimensionResource(R.dimen.spacing_extra_small)
                ).widthIn(max = 200.dp),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GenreFilterBottomSheet(
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
            .padding(dimensionResource(R.dimen.spacing_medium))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterTitle(stringResource(R.string.filter_by_genre))
            CloseIcon(
                onClick = {
                    tagClick(TagHome.Detail.CLOSE_GENRE_FILTER)
                    closeAction.invoke()
                }
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        FilterGenres(genres, filters) {
            if (filters.genreId == it.genreId) {
                inFiltering.invoke(filters.copy(genreId = null))
            } else {
                inFiltering.invoke(it)
                closeAction.invoke()
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
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
                                    tagClick("${TagMediaManager.Detail.SELECT_MEDIA_TYPE}${type.key}")
                                    onSelectMedia(filters.copy(mediaType = type, genreId = null))
                                }
                            }
                        }
                    }
                }
                MediaType.TV_SHOW.key -> {
                    ClosableFilterButton(
                        buttonText = stringResource(R.string.tv_show),
                        isActivated = true,
                        onClick = {
                            tagClick(TagHome.Detail.UNSELECT_MEDIA_TYPE_TV)
                            onClearFilter.invoke()
                        }
                    )
                    SelectGenreButton(filters, genres) {
                        tagClick(TagHome.Detail.OPEN_GENRE_FILTER)
                        onOpenGenreFilter.invoke()
                    }
                }
                MediaType.MOVIE.key -> {
                    ClosableFilterButton(
                        buttonText = stringResource(R.string.movies),
                        isActivated = true,
                        onClick = {
                            tagClick(TagHome.Detail.UNSELECT_MEDIA_TYPE_MOVIE)
                            onClearFilter.invoke()
                        }
                    )
                    SelectGenreButton(filters, genres) {
                        tagClick(TagHome.Detail.OPEN_GENRE_FILTER)
                        onOpenGenreFilter.invoke()
                    }
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
                painter = painterResource(id = R.drawable.ic_arrow_down),
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
    val tagGenreClick = { isActive: Boolean, itemId: Long ->
        val detail = if (isActive) TagHome.Detail.UNSELECT_GENRE else TagHome.Detail.SELECT_GENRE
        tagClick(detail, itemId)
    }
    Column {
        FlowRow(
            verticalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_small)),
            horizontalArrangement = Arrangement.Start
        ) {
            genres.forEach { genre ->
                val isActive = filters.genreId == genre.apiId
                ClosableFilterButton(
                    buttonText = genre.nameTranslation(),
                    isActivated = isActive
                ) {
                    tagGenreClick.invoke(isActive, genre.apiId)
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
