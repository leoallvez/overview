package br.com.deepbyte.overview.ui.media

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.PersonDetails
import br.com.deepbyte.overview.data.model.provider.ProviderPlace
import br.com.deepbyte.overview.ui.*
import br.com.deepbyte.overview.ui.navigation.events.MediaDetailsScreenEvents
import br.com.deepbyte.overview.ui.theme.AccentColor
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground
import br.com.deepbyte.overview.ui.theme.SecondaryBackground
import br.com.deepbyte.overview.util.createDiscoverParams
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
        media.apply {
            ProvidersList(providers) { provider ->
                val params = provider.createDiscoverParams(media)
                events.onNavigateToProviderDiscover(params.toJson())
            }
            when (media) {
                is Movie -> {
                    MovieReleaseYearAndRunTime(media.getReleaseYear(), media.getRuntime())
                    Director(media.getDirectorName())
                }
                is TvShow -> {
                    NumberSeasonsAndEpisodes(media.numberOfSeasons, media.numberOfEpisodes)
                    EpisodesRunTime(media.getRuntime())
                }
            }
            GenreList(genres) { genre ->
                val params = genre.createDiscoverParams(media)
                events.onNavigateToGenreDiscover(params.toJson())
            }
            BasicParagraph(R.string.synopsis, overview)
            AdsMediumRectangle(
                prodBannerId = R.string.media_details_banner,
                isVisible = showAds
            )
            CastList(getOrderedCast()) { apiId ->
                events.onNavigateToCastDetails(apiId = apiId)
            }
            MediaList(
                listTitle = stringResource(R.string.related),
                medias = getSimilarMedia()
            ) { apiId, mediaType ->
                events.onNavigateToMediaDetails(apiId = apiId, mediaType = mediaType, backToHome = true)
            }
        }
    }
}

@Composable
fun Director(directorNome: String) {
    if (directorNome.isNotEmpty()) {
        Text(
            text = stringResource(R.string.director, directorNome),
            color = AccentColor,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding))
        )
    }
}

@Composable
fun NumberSeasonsAndEpisodes(numberOfSeasons: Int, numberOfEpisodes: Int) {
    if (numberOfSeasons > 0) {
        val padding = dimensionResource(R.dimen.screen_padding)
        Row(
            modifier = Modifier
                .padding(horizontal = padding)
                .padding(top = padding)
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
        Row(
            modifier = Modifier
                .padding(horizontal = padding)
                .padding(bottom = 20.dp)
        ) {
            SimpleSubtitle2(text = stringResource(R.string.per_episode, runtime))
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
fun MovieReleaseYearAndRunTime(releaseYear: String, runtime: String) {
    if (releaseYear.isNotEmpty().or(runtime.isNotEmpty())) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding))
        ) {
            val spacerModifier = Modifier.padding(horizontal = 2.dp)
            SimpleSubtitle2(text = releaseYear)
            Spacer(modifier = spacerModifier)
            PartingPoint(
                display = releaseYear.isNotEmpty().and(runtime.isNotEmpty())
            )
            Spacer(modifier = spacerModifier)
            SimpleSubtitle2(text = runtime)
        }
    }
}

@Composable
fun ProvidersList(providers: List<ProviderPlace>, onClickItem: (ProviderPlace) -> Unit) {
    BasicTitle(stringResource(R.string.where_to_watch))
    if (providers.isNotEmpty()) {
        LazyRow(
            Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding)),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.screen_padding)
            )
        ) {
            items(providers) { provider ->
                ProviderItem(provider) {
                    onClickItem.invoke(provider)
                }
            }
        }
    } else {
        EmptyListProvidersMsg()
    }
}

@Composable
fun EmptyListProvidersMsg() {
    Row(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.screen_padding))
            .height(50.dp)
            .border(
                dimensionResource(R.dimen.border_width),
                Gray,
                RoundedCornerShape(dimensionResource(R.dimen.corner))
            ).background(SecondaryBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.empty_list_providers),
            modifier = Modifier.emptyListPadding(end = 0.dp),
            color = Gray
        )
        Icon(
            painter = painterResource(id = R.drawable.outline_alert),
            tint = Gray,
            contentDescription = stringResource(R.string.empty_list_providers),
            modifier = Modifier.emptyListPadding()
        )
    }
}

@Composable
private fun Modifier.emptyListPadding(
    start: Dp = dimensionResource(id = R.dimen.screen_padding),
    top: Dp = 5.dp,
    end: Dp = dimensionResource(id = R.dimen.screen_padding),
    bottom: Dp = 5.dp
): Modifier = padding(start, top, end, bottom)

@Composable
fun ProviderItem(provider: ProviderPlace, onClick: () -> Unit) {
    BasicImage(
        url = provider.getLogoImage(),
        contentDescription = provider.name,
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
