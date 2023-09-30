package br.dev.singular.overview.ui.media

import androidx.annotation.StringRes
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.ui.AdsMediumRectangle
import br.dev.singular.overview.ui.Backdrop
import br.dev.singular.overview.ui.BasicParagraph
import br.dev.singular.overview.ui.BasicText
import br.dev.singular.overview.ui.BasicTitle
import br.dev.singular.overview.ui.ErrorScreen
import br.dev.singular.overview.ui.MediaList
import br.dev.singular.overview.ui.PartingPoint
import br.dev.singular.overview.ui.PersonImageCircle
import br.dev.singular.overview.ui.ScreenNav
import br.dev.singular.overview.ui.SimpleSubtitle2
import br.dev.singular.overview.ui.StreamingIcon
import br.dev.singular.overview.ui.ToolbarButton
import br.dev.singular.overview.ui.ToolbarTitle
import br.dev.singular.overview.ui.TrackScreenView
import br.dev.singular.overview.ui.UiStateResult
import br.dev.singular.overview.ui.nameTranslation
import br.dev.singular.overview.ui.navigation.wrappers.MediaDetailsNavigate
import br.dev.singular.overview.ui.theme.AccentColor
import br.dev.singular.overview.ui.theme.Gray
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.util.defaultBorder
import br.dev.singular.overview.util.defaultPadding
import br.dev.singular.overview.util.toJson
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import timber.log.Timber

@Composable
fun MediaDetailsScreen(
    params: Pair<Long, String>,
    navigate: MediaDetailsNavigate,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.MediaDetails, tracker = viewModel.analyticsTracker)

    val (apiId: Long, mediaType: String) = params
    val type = MediaTypeEnum.getByKey(mediaType)
    viewModel.loadMediaDetails(apiId, type)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId, type) }
    ) { media ->
        MediaDetailsContent(media, viewModel.showAds, navigate) {
            viewModel.refresh(apiId, type)
        }
    }
}

@Composable
fun MediaDetailsContent(
    media: Media?,
    showAds: Boolean,
    navigate: MediaDetailsNavigate,
    onRefresh: () -> Unit
) {
    if (media == null) {
        ErrorScreen { onRefresh.invoke() }
    } else {
        CollapsingToolbarScaffold(
            modifier = Modifier,
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                MediaToolBar(media) { navigate.popBackStack() }
            }
        ) {
            MediaBody(media, showAds, navigate)
        }
    }
}

@Composable
fun MediaToolBar(media: Media, backButtonAction: () -> Unit) {
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
            ToolbarButton(
                painter = Icons.Default.KeyboardArrowLeft,
                descriptionResource = R.string.back_to_home_icon,
                modifier = Modifier.padding(dimensionResource(R.dimen.default_padding))
            ) { backButtonAction.invoke() }
        }
    }
}

@Composable
fun MediaBody(
    media: Media,
    showAds: Boolean,
    navigate: MediaDetailsNavigate
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PrimaryBackground)
            .padding(dimensionResource(R.dimen.default_padding))
    ) {
        StreamingsOverview(media.streamings, media.isReleased()) { streaming ->
            val streamingJson = streaming.toJson()
            Timber.tag("streaming_json").d("streaming json: $streamingJson")
            navigate.toStreamingExplore(streamingJson)
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
            navigate.toCastDetails(apiId = apiId)
        }
        MediaList(
            listTitle = stringResource(R.string.related),
            medias = media.getSimilarMedia()
        ) { apiId, mediaType ->
            navigate.toMediaDetails(apiId = apiId, mediaType = mediaType, backToHome = true)
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
fun StreamingsOverview(
    streamings: List<StreamingEntity>,
    isReleased: Boolean,
    onClickItem: (StreamingEntity) -> Unit
) {
    BasicTitle(stringResource(R.string.where_to_watch))
    if (streamings.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding)),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            items(streamings) { streaming ->
                StreamingIcon(streaming = streaming) {
                    onClickItem.invoke(streaming)
                }
            }
        }
    } else {
        StreamingsNotFound(
            if (isReleased) {
                R.string.empty_list_providers
            } else {
                R.string.not_yet_released
            }
        )
    }
}

@Composable
fun StreamingsNotFound(@StringRes stringResource: Int) {
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
            backgroundColor = AccentColor
        )
    ) {
        Text(
            text = name,
            color = PrimaryBackground,
            style = MaterialTheme.typography.caption,
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
            style = MaterialTheme.typography.caption,
            isBold = true
        )
        BasicText(
            text = castPerson.getCharacterName(),
            style = MaterialTheme.typography.caption,
            color = AccentColor
        )
    }
}