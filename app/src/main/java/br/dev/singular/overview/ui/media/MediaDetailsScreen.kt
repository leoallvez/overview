package br.dev.singular.overview.ui.media

import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.model.media.Video
import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagMedia
import br.dev.singular.overview.presentation.tagging.params.TagPerson
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.ui.AdsMediumRectangle
import br.dev.singular.overview.ui.Backdrop
import br.dev.singular.overview.ui.BasicParagraph
import br.dev.singular.overview.ui.ButtonWithIcon
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.PartingPoint
import br.dev.singular.overview.ui.PersonImageCircle
import br.dev.singular.overview.ui.SimpleSubtitle2
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.DarkGray
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import br.dev.singular.overview.util.defaultBorder
import br.dev.singular.overview.util.defaultPadding
import br.dev.singular.overview.util.toJson
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import timber.log.Timber
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

private fun tagClick(detail: String, id: Long = 0L) {
    TagManager.logClick(TagMedia.PATH, detail, id)
}

@Composable
fun MediaDetailsScreen(
    params: Pair<Long, String>,
    navigate: MediaDetailsNavigate,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    val (apiId: Long, mediaType: String) = params
    val type = MediaType.getByKey(mediaType)
    val onRefresh = { viewModel.load(apiId, type) }
    LaunchedEffect(true) {
        onRefresh.invoke()
    }

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        tagPath = TagMedia.PATH,
        onRefresh = onRefresh
    ) { media ->
        val isLiked = remember { mutableStateOf(media?.isLiked ?: false) }

        val onLike = {
            isLiked.value = !isLiked.value
            tagClick(if (isLiked.value) {
                TagMedia.Detail.LIKE_ACTIVATING
            } else {
                TagMedia.Detail.LIKE_DEACTIVATING
            })
            viewModel.updateLike(media, isLiked.value)
        }

        MediaDetailsContent(
            media = media,
            showAds = viewModel.showAds,
            isLiked = isLiked.value,
            navigate = navigate,
            onRefresh = onRefresh,
            onLikeClick = onLike
        ) { streaming ->
            Timber.tag("stream_view").i(message = "streaming: $streaming")
            tagClick(TagCommon.Detail.SELECT_STREAMING, streaming.apiId)
            viewModel.saveSelectedStream(streaming.toJson())
        }
    }
}

@Composable
fun MediaDetailsContent(
    media: Media?,
    showAds: Boolean,
    isLiked: Boolean,
    navigate: MediaDetailsNavigate,
    onRefresh: () -> Unit,
    onLikeClick: () -> Unit,
    onClickStreaming: (StreamingEntity) -> Unit
) {
    if (media == null) {
        ErrorScreen(TagMedia.PATH) { onRefresh.invoke() }
    } else {
        CollapsingToolbarScaffold(
            modifier = Modifier,
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                MediaToolBar(
                    media = media,
                    isLiked = isLiked,
                    onLikeClick = onLikeClick::invoke,
                    onBackstackClick = navigate::popBackStack,
                    onBackstackLongClick = navigate::toHome
                )
            }
        ) {
            MediaBody(media, showAds, navigate, onClickStreaming)
        }
    }
}

@Composable
fun MediaToolBar(
    media: Media,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onBackstackClick: () -> Unit,
    onBackstackLongClick: () -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
        media.apply {
            Backdrop(
                url = getBackdropImage(),
                contentDescription = getLetter(),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
            ToolbarTitle(
                title = getLetter(),
                textPadding = PaddingValues(horizontal = dimensionResource(R.dimen.spacing_4x)),
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonWithIcon(
                    painter = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    descriptionResource = R.string.backstack_icon,
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
                    withBorder = false,
                    onClick = {
                        tagClick(TagCommon.Detail.BACK)
                        onBackstackClick.invoke()
                    },
                    onLongClick = {
                        tagClick(TagCommon.Detail.BACK)
                        onBackstackLongClick.invoke()
                    }

                )
                LikeButton(isLiked = isLiked, onLikeClick)
            }
        }
    }
}

@Composable
fun MediaBody(
    media: Media,
    showAds: Boolean,
    navigate: MediaDetailsNavigate,
    onClickStreaming: (StreamingEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PrimaryBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
        ) {
            StreamingOverview(media.streamings, media.isReleased()) { streaming ->
                onClickStreaming(streaming)
                navigate.toHome()
            }
            Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.spacing_1x)))
            Info(stringResource(R.string.release_date), media.getFormattedReleaseDate())
            when (media) {
                is Movie -> {
                    Info(stringResource(R.string.runtime), media.getRuntime())
                    Info(stringResource(R.string.director), media.getDirectorName(), AccentColor)
                }

                is TvShow -> {
                    NumberSeasonsAndEpisodes(media.numberOfSeasons, media.numberOfEpisodes)
                    EpisodesRunTime(media.getRuntime())
                }
            }
            Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.spacing_1x)))
            GenreList(media.genres)
            BasicParagraph(R.string.synopsis, media.overview)
            AdsMediumRectangle(
                prodBannerId = R.string.media_details_banner,
                isVisible = showAds
            )
        }
        VideoList(media.videos) { videoKey ->
            tagClick(TagMedia.Detail.VIDEO)
            navigate.toYouTubePlayer(videoKey = videoKey)
        }
        CastList(media.getOrderedCast()) { apiId ->
            tagClick(TagMedia.Detail.CAST, apiId)
            navigate.toPersonDetails(apiId = apiId)
        }
        UiMediaList(
            title = stringResource(R.string.related),
            contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
            items = media.getSimilarMedia(),
            onClick = {
                TagMediaManager.logClick(TagPerson.PATH, it.id)
                navigate.toMediaDetails(it)
            }
        )
    }
}

@Composable
fun NumberSeasonsAndEpisodes(numberOfSeasons: Int, numberOfEpisodes: Int) {
    if (numberOfSeasons > 0) {
        Row {
            val spacerModifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_1x))
            NumberOfSeasons(numberOfSeasons)
            Spacer(modifier = spacerModifier)
            PartingPoint()
            Spacer(modifier = spacerModifier)
            NumberOfEpisodes(numberOfEpisodes)
        }
    }
}

@Composable
fun EpisodesRunTime(runtime: String) {
    if (runtime.isNotEmpty()) {
        SimpleSubtitle2(text = stringResource(R.string.runtime_per_episode, runtime))
    }
}

@Composable
fun NumberOfSeasons(numberOfSeasons: Int) {
    val seasonsLabel = if (numberOfSeasons > 1) R.string.n_seasons else R.string.one_season
    SimpleSubtitle2(stringResource(id = seasonsLabel, numberOfSeasons))
}

@Composable
fun NumberOfEpisodes(numberOfEpisodes: Int) {
    val episodesLabel = if (numberOfEpisodes > 1) R.string.n_episodes else R.string.one_episode
    SimpleSubtitle2(stringResource(id = episodesLabel, numberOfEpisodes))
}

@Composable
fun Info(label: String = "", info: String, color: Color = Color.White) {
    if (info.isNotEmpty()) {
        SimpleSubtitle2(
            text = if (label.isNotEmpty()) "$label: $info" else info,
            color = color
        )
    }
}

@Composable
fun StreamingOverview(
    streaming: List<StreamingEntity>,
    isReleased: Boolean,
    onClickItem: (StreamingEntity) -> Unit
) {
    UiTitle(stringResource(R.string.where_to_watch))
    if (streaming.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_2x)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
        ) {
            items(streaming) { streaming ->
                StreamingIcon(streaming = streaming) {
                    tagClick(TagCommon.Detail.SELECT_STREAMING, streaming.apiId)
                    onClickItem.invoke(streaming)
                }
            }
        }
    } else {
        StreamingNotFound(
            if (isReleased) {
                R.string.empty_list_providers
            } else {
                R.string.not_yet_released
            }
        )
    }
}

@Composable
fun StreamingNotFound(@StringRes stringResource: Int) {
    Row(
        modifier = Modifier
            .padding(vertical = dimensionResource(R.dimen.spacing_1x))
            .height(dimensionResource(R.dimen.spacing_10x))
            .defaultBorder(DarkGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(stringResource),
            modifier = Modifier.defaultPadding(end = 0.dp),
            color = Gray
        )
        Icon(
            painter = painterResource(R.drawable.ic_outline_alert),
            tint = Gray,
            contentDescription = stringResource(stringResource),
            modifier = Modifier.defaultPadding()
        )
    }
}

@Composable
fun GenreList(genres: List<GenreEntity>) {
    if (genres.isNotEmpty()) {
        LazyRow(
            Modifier.padding(
                vertical = dimensionResource(R.dimen.spacing_1x)
            ),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
        ) {
            items(genres) { genre ->
                GenreItem(name = genre.nameTranslation()) {
                    tagClick(TagMedia.Detail.SELECT_GENRE, genre.apiId)
                }
            }
        }
    }
}

@Composable
fun GenreItem(name: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(percent = 100),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.spacing_1x)
        ),
        modifier = Modifier.height(dimensionResource(R.dimen.spacing_7x)),
        colors = ButtonDefaults.buttonColors(
            contentColor = AccentColor,
            containerColor = AccentColor
        ),
        border = BorderStroke(dimensionResource(R.dimen.border_width), AccentColor)
    ) {
        Text(
            text = name,
            color = PrimaryBackground,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CastList(cast: List<Person>, onClickItem: (Long) -> Unit) {
    if (cast.isNotEmpty()) {
        val contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x))
        Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_4x))) {
            UiTitle(
                stringResource(R.string.cast),
                modifier = Modifier.padding(contentPadding)
            )
            LazyRow(
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement
                    .spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                items(cast) { castPerson ->
                    CastItem(castPerson = castPerson) {
                        onClickItem.invoke(castPerson.apiId)
                    }
                }
            }
        }
    }
}

@Composable
fun VideoList(videos: List<Video>, onClick: (videoKey: String) -> Unit) {
    if (videos.isNotEmpty()) {
        val contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x))
        UiTitle(
            stringResource(R.string.videos),
            modifier = Modifier.padding(contentPadding)
        )
        LazyRow (
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_2x))
        ) {
            items(videos) { video ->
                VideoItem(video = video) {
                    onClick.invoke(it)
                }
            }
        }
    }
}

@Composable
fun VideoItem(video: Video, onClick: (String) -> Unit) {
    Column {
        val with = 300.dp
        val iconAlpha = 0.8f
        Box(
            modifier = Modifier
                .width(with)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
                .clickable { onClick(video.key) }
                .background(PrimaryBackground)
                .then(Modifier.border())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.getThumbnailImage())
                    .crossfade(true)
                    .build(),
                contentDescription = video.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(SecondaryBackground.copy(alpha = iconAlpha))
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    tint = AccentColor.copy(alpha = iconAlpha),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.spacing_6x))
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_1x)))
        Text(
            text = video.name,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(with)
        )
    }
}

@Composable
fun CastItem(castPerson: Person, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        PersonImageCircle(castPerson)
        UiText(
            text = castPerson.name,
            modifier = Modifier.width(120.dp)
                .padding(top = dimensionResource(R.dimen.spacing_1x)),
            isBold = true
        )
        UiText(
            text = castPerson.getCharacterName(),
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodySmall,
            color = AccentColor
        )
    }
}

@Composable
fun LikeButton(
    isLiked: Boolean,
    onClick: () -> Unit
) {
    val buttonSize = 35.dp
    val duration = 200
    val unlikedColor = Gray
    val likedColor = AlertColor
    val pulsationScale = if (isLiked) 0.9f else 0.7f

    val background = remember { Animatable(unlikedColor) }
    LaunchedEffect(isLiked) {
        val color = if (isLiked) likedColor else unlikedColor
        background.animateTo(color, tween(duration))
    }

    val scale by animateFloatAsState(
        targetValue = pulsationScale,
        tween(durationMillis = duration, easing = LinearEasing),
        label = "PulsatingScale"
    )

    Box(
        modifier = Modifier
            .padding(PaddingValues(dimensionResource(R.dimen.spacing_4x)))
            .clip(CircleShape)
            .background(PrimaryBackground.copy(alpha = if (isLiked) 0.8f else 0.6f))
            .size(buttonSize)
            .border(dimensionResource(R.dimen.border_width),
                if (isLiked) likedColor else unlikedColor,
                CircleShape
            )
            .clickable { onClick.invoke() }
    ) {
        Box(modifier = Modifier.size(buttonSize)) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(id = R.string.like_button),
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(scale),
                tint = background.value
            )
        }
    }
}
