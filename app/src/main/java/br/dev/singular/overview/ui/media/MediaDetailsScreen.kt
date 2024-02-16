package br.dev.singular.overview.ui.media

import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.singular.overview.R
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.ui.AdsMediumRectangle
import br.dev.singular.overview.ui.Backdrop
import br.dev.singular.overview.ui.BasicParagraph
import br.dev.singular.overview.ui.BasicText
import br.dev.singular.overview.ui.BasicTitle
import br.dev.singular.overview.ui.ButtonWithIcon
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.MediaList
import br.dev.singular.overview.ui.PartingPoint
import br.dev.singular.overview.ui.PersonImageCircle
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SimpleSubtitle2
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.AlertColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.defaultBorder
import br.dev.singular.overview.util.defaultPadding
import br.dev.singular.overview.util.toJson
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MediaDetailsScreen(
    params: Pair<Long, String>,
    navigate: MediaDetailsNavigate,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.MediaDetails, tracker = viewModel.analyticsTracker)

    val (apiId: Long, mediaType: String) = params
    val type = MediaType.getByKey(mediaType)
    val onRefresh = { viewModel.load(apiId, type) }
    LaunchedEffect(true) {
        onRefresh.invoke()
    }

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = onRefresh
    ) { media ->
        val isLiked = remember { mutableStateOf(media?.isLiked ?: false) }

        val onLike = {
            isLiked.value = !isLiked.value
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
        ErrorScreen { onRefresh.invoke() }
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
                    onBackstackLongClick = navigate::toExploreStreaming
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
    onBackstackLongClick: () -> Unit,
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
                textPadding = PaddingValues(start = dimensionResource(R.dimen.screen_padding)),
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonWithIcon(
                    painter = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    descriptionResource = R.string.backstack_icon,
                    modifier = Modifier.padding(dimensionResource(R.dimen.default_padding)),
                    onClick = onBackstackClick::invoke,
                    onLongClick = onBackstackLongClick::invoke

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
            .padding(dimensionResource(R.dimen.default_padding))
    ) {
        StreamingOverview(media.streamings, media.isReleased()) { streaming ->
            onClickStreaming(streaming)
            navigate.toExploreStreaming()
        }
        MediaSpace()
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
        MediaSpace()
        GenreList(media.genres)
        BasicParagraph(R.string.synopsis, media.overview)
        AdsMediumRectangle(
            prodBannerId = R.string.media_details_banner,
            isVisible = showAds
        )
        CastList(media.getOrderedCast()) { apiId ->
            navigate.toPersonDetails(apiId = apiId)
        }
        MediaList(
            listTitle = stringResource(R.string.related),
            medias = media.getSimilarMedia()
        ) { apiId, mediaType ->
            navigate.toMediaDetails(apiId = apiId, mediaType = mediaType)
        }
    }
}

@Composable
fun MediaSpace() {
    Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.default_padding)))
}

@Composable
fun NumberSeasonsAndEpisodes(numberOfSeasons: Int, numberOfEpisodes: Int) {
    if (numberOfSeasons > 0) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.screen_padding))
                .padding(top = 2.dp)
        ) {
            val spacerModifier = Modifier.padding(horizontal = 2.dp)
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
        val padding = dimensionResource(R.dimen.screen_padding)
        Row(modifier = Modifier.padding(horizontal = padding)) {
            SimpleSubtitle2(text = stringResource(R.string.runtime_per_episode, runtime))
        }
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
        Row(
            Modifier.padding(
                horizontal = dimensionResource(R.dimen.screen_padding),
                vertical = 2.dp
            )
        ) {
            SimpleSubtitle2(
                text = if (label.isNotEmpty()) "$label: $info" else info,
                color = color
            )
        }
    }
}

@Composable
fun StreamingOverview(
    streaming: List<StreamingEntity>,
    isReleased: Boolean,
    onClickItem: (StreamingEntity) -> Unit
) {
    BasicTitle(stringResource(R.string.where_to_watch))
    if (streaming.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding)),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            items(streaming) { streaming ->
                StreamingIcon(streaming = streaming) {
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
            .padding(
                horizontal = dimensionResource(R.dimen.screen_padding),
                vertical = dimensionResource(R.dimen.default_padding)
            )
            .height(dimensionResource(R.dimen.streaming_item_small_size))
            .defaultBorder(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(stringResource),
            modifier = Modifier.defaultPadding(end = 0.dp),
            color = Gray
        )
        Icon(
            painter = painterResource(R.drawable.outline_alert),
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
                vertical = dimensionResource(R.dimen.default_padding)
            ),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding)),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            items(genres) { genre ->
                GenreItem(name = genre.nameTranslation())
            }
        }
    }
}

@Composable
fun GenreItem(name: String) {
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(percent = 100),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.default_padding)
        ),
        modifier = Modifier
            .height(25.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = AccentColor,
            containerColor = AccentColor
        )
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
        Column {
            BasicTitle(title = stringResource(R.string.cast))
            LazyRow(
                contentPadding = PaddingValues(
                    vertical = dimensionResource(R.dimen.screen_padding)
                )
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
fun CastItem(castPerson: Person, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        PersonImageCircle(castPerson)
        BasicText(
            text = castPerson.name,
            style = MaterialTheme.typography.bodySmall,
            isBold = true
        )
        BasicText(
            text = castPerson.getCharacterName(),
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
    val buttonSize = 40.dp
    val duration = 200
    val unlikedColor = Gray
    val likedColor = AlertColor
    val pulsationScale = if (isLiked) 1.2f else 1f

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
            .padding(PaddingValues(dimensionResource(R.dimen.screen_padding_new)))
            .clip(CircleShape)
            .background(PrimaryBackground.copy(alpha = if (isLiked) 0.8f else 0.6f))
            .size(buttonSize)
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
