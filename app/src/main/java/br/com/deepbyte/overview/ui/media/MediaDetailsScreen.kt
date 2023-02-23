package br.com.deepbyte.overview.ui.media

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.PersonDetails
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.MediaDetailsScreenEvents
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.util.createDiscoverParams
import br.com.deepbyte.overview.util.defaultBackground
import br.com.deepbyte.overview.util.emptyListPadding
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MediaDetailsScreen(
    params: Pair<Long, String>,
    events: MediaDetailsScreenEvents,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    TrackScreenView(screen = ScreenNav.MediaDetails, tracker = viewModel.analyticsTracker)

    val (apiId: Long, mediaType: String) = params
    viewModel.loadMediaDetails(apiId, mediaType)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId, mediaType) }
    ) { media ->
        MediaDetailsContent(media, viewModel.showAds, events) {
            viewModel.refresh(apiId, mediaType)
        }
    }
}

@Composable
fun MediaDetailsContent(
    media: Media?,
    showAds: Boolean,
    events: MediaDetailsScreenEvents,
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
                MediaToolBar(media) { events.onPopBackStack() }
            }
        ) {
            MediaBody(media, showAds, events)
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
                textPadding = dimensionResource(R.dimen.default_padding),
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
    events: MediaDetailsScreenEvents
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PrimaryBackground)
            .padding(dimensionResource(R.dimen.default_padding))
    ) {
        StreamingList(media.streamings, media.isReleased()) { streaming ->
            val params = streaming.createDiscoverParams(media)
            events.onNavigateToProviderDiscover(params.toJson())
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
        GenreList(media.genres) { genre ->
            val params = genre.createDiscoverParams(media)
            events.onNavigateToGenreDiscover(params.toJson())
        }
        BasicParagraph(R.string.synopsis, media.overview)
        AdsMediumRectangle(
            prodBannerId = R.string.media_details_banner,
            isVisible = showAds
        )
        CastList(media.getOrderedCast()) { apiId ->
            events.onNavigateToCastDetails(apiId = apiId)
        }
        MediaList(
            listTitle = stringResource(R.string.related),
            medias = media.getSimilarMedia()
        ) { apiId, mediaType ->
            events.onNavigateToMediaDetails(apiId = apiId, mediaType = mediaType, backToHome = true)
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
        val padding = dimensionResource(R.dimen.screen_padding)
        Row(modifier = Modifier.padding(horizontal = padding).padding(top = 2.dp)) {
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
fun StreamingList(
    streamings: List<Streaming>,
    isReleased: Boolean,
    onClickItem: (Streaming) -> Unit
) {
    if (streamings.isNotEmpty()) {
        BasicTitle(stringResource(R.string.where_to_watch))
        LazyRow(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.screen_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding)),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            items(streamings) { streaming ->
                StreamingItem(streaming) {
                    onClickItem.invoke(streaming)
                }
            }
        }
    } else {
        StreamingListEmptyMsg(
            if (isReleased) { R.string.empty_list_providers } else { R.string.not_yet_released }
        )
    }
}

@Composable
fun StreamingListEmptyMsg(@StringRes stringResource: Int) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(R.dimen.screen_padding),
                vertical = dimensionResource(R.dimen.default_padding)
            )
            .height(40.dp)
            .defaultBackground()
            .background(PrimaryBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(stringResource),
            modifier = Modifier.emptyListPadding(end = 0.dp),
            color = Gray
        )
        Icon(
            painter = painterResource(R.drawable.outline_alert),
            tint = Gray,
            contentDescription = stringResource(stringResource),
            modifier = Modifier.emptyListPadding()
        )
    }
}

@Composable
fun StreamingItem(streaming: Streaming, onClick: () -> Unit) {
    BasicImage(
        url = streaming.getLogoImage(),
        contentDescription = streaming.name,
        withBorder = true,
        modifier = Modifier
            .size(50.dp)
            .clickable { onClick.invoke() }
    )
}

@Composable
fun GenreList(genres: List<Genre>, onClickItem: (Genre) -> Unit) {
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
                GenreItem(name = genre.name) {
                    onClickItem.invoke(genre)
                }
            }
        }
    }
}

@Composable
fun GenreItem(name: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = { onClick.invoke() },
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
fun CastList(cast: List<PersonDetails>, onClickItem: (Long) -> Unit) {
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
fun CastItem(castPerson: PersonDetails, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        PersonImageCircle(imageUrl = castPerson.getProfileImage(), contentDescription = castPerson.name)
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
