package br.dev.singular.overview.ui.home

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagHome
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.UiIcon
import br.dev.singular.overview.presentation.ui.components.UiIconButton
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.screens.common.ErrorScreen
import br.dev.singular.overview.presentation.ui.screens.common.LoadingScreen
import br.dev.singular.overview.presentation.ui.screens.common.NothingFoundScreen
import br.dev.singular.overview.presentation.ui.screens.common.TrackScreenView
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.HomeNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.DarkGray
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.animatedBorder
import kotlinx.coroutines.launch

private fun tagClick(detail: String, id: Long = 0L) {
    TagManager.logClick(TagHome.PATH, detail, id)
}

@Composable
fun HomeScreen(
    navigate: HomeNavigate,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val items = viewModel.medias.collectAsLazyPagingItems()
    val isNotLoading = items.loadState.refresh != LoadState.Loading

    HomeContent(
        navigate = navigate,
        showHighlightIcon = viewModel.showHighlightIcon.collectAsState().value && isNotLoading,
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
    showHighlightIcon: Boolean,
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
        sheetShape = RoundedCornerShape(
            topStart = dimensionResource(R.dimen.spacing_4x),
            topEnd = dimensionResource(R.dimen.spacing_4x))
        ,
        modifier = Modifier.fillMaxSize()
    ) {
        UiScaffold(
            topBar = {
                HomeToolBar(
                    filters = filters,
                    genres = genres,
                    navigate = navigate,
                    showHighlightIcon = showHighlightIcon,
                    openGenreFilter = closeFilterBottomSheet,
                    onSelectMediaType = inFiltering
                )
            }
        ) { padding ->
            val topPadding = padding.calculateTopPadding()
            when (items.loadState.refresh) {
                is LoadState.Loading -> LoadingScreen(
                    tagPath = TagHome.PATH,
                    modifier = Modifier.padding(top = topPadding)
                )
                is LoadState.NotLoading -> {
                    if (items.itemCount == 0) {
                        ErrorScreen(
                            tagPath = TagHome.PATH,
                            modifier = Modifier.padding(top = topPadding),
                            onRefresh = onRefresh
                        )
                    } else {
                        TrackScreenView(TagHome.PATH, TagStatus.SUCCESS)
                        UiMediaGrid(
                            items = items,
                            modifier = Modifier
                                .background(PrimaryBackground)
                                .padding(top = topPadding)
                                .fillMaxSize(),
                            onClick = {
                                TagMediaManager.logMediaClick(TagHome.PATH, it.id)
                                navigate.toMediaDetails(it)
                            }
                        )
                    }
                }
                else -> {
                    NothingFoundScreen(
                        tagPath = TagHome.PATH,
                        modifier = Modifier.padding(top = topPadding),
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
    showHighlightIcon: Boolean,
    openGenreFilter: () -> Unit,
    onSelectMediaType: (SearchFilters) -> Unit,
    navigate: HomeNavigate
) {
    Column(
        modifier = Modifier
            .background(PrimaryBackground)
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.spacing_2x))
    ) {
        Row(
            modifier = Modifier
                .height(dimensionResource(R.dimen.spacing_12x))
                .padding(top = dimensionResource(R.dimen.spacing_3x)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SelectStreaming(filters.streaming, showHighlightIcon) {
                tagClick(TagCommon.Detail.SELECT_STREAMING)
                navigate.toSelectStreaming()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_2x)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterMediaType(filters, genres, onSelectMediaType, openGenreFilter)
        }
    }
}

@Composable
fun SelectStreaming(
    streaming: StreamingEntity?,
    showHighlightIcon: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(dimensionResource(R.dimen.spacing_10x))
            .clickable { onClick.invoke() }
            .fillMaxWidth()
            .background(SecondaryBackground)
    ) {
        Row(
            Modifier.background(PrimaryBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StreamingIcon(
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_1x)),
                    size = dimensionResource(R.dimen.spacing_8x),
                    corner = dimensionResource(R.dimen.circle_conner),
                    streaming = streaming,
                    withBorder = false
                )
                HomeScreamTitle(title = streaming?.name ?: String())
            }
            UiIconButton(
                icon = painterResource(id = R.drawable.ic_arrow_down),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacing_1x))
                    .then(
                    if (showHighlightIcon) {
                        Modifier.animatedBorder(
                            borderColors = listOf(DarkGray, AccentColor),
                            backgroundColor = SecondaryBackground,
                            shape = CircleShape,
                            borderWidth = 2.dp,
                            animationDurationInMillis = 1500
                        )
                    } else {
                        Modifier.border(2.dp, DarkGray, CircleShape)
                    }
                ),
                iconColor = AccentColor,
                iconSize = dimensionResource(if (showHighlightIcon) R.dimen.spacing_7x else R.dimen.spacing_8x),
                background = SecondaryBackground,
                onClick = onClick
            )
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
                    horizontal = dimensionResource(R.dimen.spacing_2x),
                    vertical = dimensionResource(R.dimen.spacing_1x)
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
            .padding(dimensionResource(R.dimen.spacing_3x))
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
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_4x)))
        FilterGenres(genres, filters) {
            if (filters.genreId == it.genreId) {
                inFiltering.invoke(filters.copy(genreId = null))
            } else {
                inFiltering.invoke(it)
                closeAction.invoke()
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_4x)))
    }
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(dimensionResource(R.dimen.spacing_6x))
            .clickable { onClick.invoke() }
    ) {
        Icon(
            tint = Color.White,
            modifier = Modifier
                .size(dimensionResource(R.dimen.spacing_5x))
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.spacing_2x))
                .background(PrimaryBackground)
        ) {
            when (filters.mediaType.key) {
                MediaType.ALL.key -> {
                    UiMediaTypeSelector(type = MediaUiType.ALL) {
                        val newType = MediaType.getByKey(it.name.lowercase())
                        TagMediaManager.logTypeClick(TagHome.PATH, it)
                        onSelectMedia(filters.copy(mediaType = newType, genreId = null))
                    }
                }
                MediaType.TV_SHOW.key -> {
                    ClosableChip(
                        buttonText = stringResource(R.string.tv_show),
                        activated = true,
                        onClick = {
                            tagClick(TagHome.Detail.UNSELECT_MEDIA_TYPE_TV)
                            onClearFilter.invoke()
                        }
                    )
                    SelectGenreChip(filters, genres) {
                        tagClick(TagHome.Detail.OPEN_GENRE_FILTER)
                        onOpenGenreFilter.invoke()
                    }
                }
                MediaType.MOVIE.key -> {
                    ClosableChip(
                        buttonText = stringResource(R.string.movies),
                        activated = true,
                        onClick = {
                            tagClick(TagHome.Detail.UNSELECT_MEDIA_TYPE_MOVIE)
                            onClearFilter.invoke()
                        }
                    )
                    SelectGenreChip(filters, genres) {
                        tagClick(TagHome.Detail.OPEN_GENRE_FILTER)
                        onOpenGenreFilter.invoke()
                    }
                }
            }
        }
    }
}

@Composable
fun SelectGenreChip(filters: SearchFilters, genres: List<GenreEntity>, onClick: () -> Unit) {
    val activated = filters.genreId != null
    UiChip(
        text = if (activated) {
            genreDescription(filters.genreId, genres)
        } else {
            stringResource(R.string.genre)
        },
        activated = activated,
        onClick = onClick,
        icon = {
            UiIcon(
                icon = painterResource(id = R.drawable.ic_arrow_down),
                color = if (activated) AccentColor else Gray,
                contentDescription = stringResource(R.string.filters),
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
                .spacedBy(dimensionResource(R.dimen.spacing_2x)),
            horizontalArrangement = Arrangement.Start
        ) {
            genres.forEach { genre ->
                val activated = filters.genreId == genre.apiId
                ClosableChip(
                    buttonText = genre.nameTranslation(),
                    activated = activated
                ) {
                    tagGenreClick.invoke(activated, genre.apiId)
                    onClick.invoke(filters.copy(genreId = genre.apiId))
                }
            }
        }
    }
}

@Composable
fun ClosableChip(
    buttonText: String,
    activated: Boolean,
    onClick: () -> Unit
) {
    UiChip(
        buttonText,
        activated = activated,
        modifier = Modifier.padding(end = dimensionResource(R.dimen.spacing_2x)),
        icon = {
            if (activated) {
                UiIcon(
                    icon = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.close)
                )
            }
        },
        onClick = onClick
    )
}

@Composable
fun FilterTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}
