package br.com.deepbyte.take.ui.media

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.deepbyte.take.R
import br.com.deepbyte.take.data.MediaType
import br.com.deepbyte.take.data.api.response.Genre
import br.com.deepbyte.take.data.api.response.ProviderPlace
import br.com.deepbyte.take.data.model.DiscoverParams
import br.com.deepbyte.take.ui.*
import br.com.deepbyte.take.ui.navigation.MediaDetailsScreenEvents
import br.com.deepbyte.take.ui.theme.BlueTake
import br.com.deepbyte.take.ui.theme.PrimaryBackground
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import br.com.deepbyte.take.data.api.response.MediaDetailResponse as MediaDetails
import br.com.deepbyte.take.data.api.response.PersonResponse as Person

@Composable
fun MediaDetailsScreen(
    params: Pair<Long, String>,
    events: MediaDetailsScreenEvents,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value
    TrackScreenView(screen = ScreenNav.MediaDetails, tracker = viewModel.analyticsTracker)

    val (apiId: Long, mediaType: String) = params
    viewModel.loadMediaDetails(apiId, mediaType)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId, mediaType) }
    ) { media ->
        media?.type = mediaType
        MediaDetailsContent(media, showAds, events) {
            viewModel.refresh(apiId, mediaType)
        }
    }
}

@Composable
fun MediaDetailsContent(
    mediaDetails: MediaDetails?,
    showAds: Boolean,
    events: MediaDetailsScreenEvents,
    onRefresh: () -> Unit
) {
    if (mediaDetails == null) {
        ErrorScreen { onRefresh.invoke() }
    } else {
        CollapsingToolbarScaffold(
            modifier = Modifier,
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                MediaToolBar(mediaDetails) {
                    events.onNavigateToHome()
                }
            }
        ) {
            MediaBody(mediaDetails, showAds, events)
        }
    }
}

@Composable
fun MediaToolBar(mediaDetails: MediaDetails, backButtonAction: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        mediaDetails.apply {
            Backdrop(
                url = getBackdropImage(),
                contentDescription = getLetter(),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
            BasicImage(
                url = getPosterImage(),
                contentDescription = getLetter(),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(width = 140.dp, height = 200.dp)
                    .padding(dimensionResource(R.dimen.screen_padding))
                    .shadow(2.dp, shape = RoundedCornerShape(dimensionResource(R.dimen.corner)))
            )
            ToolbarButton(
                painter = Icons.Default.KeyboardArrowLeft,
                descriptionResource = R.string.back_to_home_icon
            ) { backButtonAction.invoke() }
        }
    }
}

@Composable
fun MediaBody(
    mediaDetails: MediaDetails,
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
        mediaDetails.apply {
            ScreenTitle(text = getLetter())
            ProvidersList(providers) { provider ->
                val params = DiscoverParams.create(provider, mediaDetails)
                events.onNavigateToProviderDiscover(params.toJson())
            }
            if (mediaDetails.type == MediaType.MOVIE.key) {
                MovieReleaseYearAndRunTime(getReleaseYear(), getMovieRuntime())
            } else {
                NumberSeasonsAndEpisodes(numberOfSeasons, numberOfEpisodes)
                EpisodesRunTime(getEpisodesRuntime())
            }
            GenreList(genres) { genre ->
                val params = DiscoverParams.create(genre, mediaDetails)
                events.onNavigateToGenreDiscover(params.toJson())
            }
            BasicParagraph(R.string.synopsis, overview)
            AdsMediumRectangle(
                prodBannerId = R.string.banner_sample_id,
                isVisible = showAds
            )
            CastList(getOrderedCast()) { apiId ->
                events.onNavigateToCastDetails(apiId = apiId)
            }
            MediaItemList(
                listTitle = stringResource(R.string.related),
                items = similar.results
            ) { apiId, mediaType ->
                events.onNavigateToMediaDetails(apiId = apiId, mediaType = mediaType)
            }
        }
    }
}

@Composable
fun NumberSeasonsAndEpisodes(numberOfSeasons: Int, numberOfEpisodes: Int) {
    if (numberOfSeasons > 0) {
        val padding = dimensionResource(R.dimen.screen_padding)
        Row(modifier = Modifier.padding(horizontal = padding).padding(top = padding)) {
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
            modifier = Modifier.padding(horizontal = padding).padding(bottom = 20.dp)
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
    if (providers.isNotEmpty()) {
        BasicTitle(stringResource(R.string.where_to_watch))
        LazyRow(
            Modifier.padding(
                vertical = 10.dp
            ),
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
    }
}

@Composable
fun ProviderItem(provider: ProviderPlace, onClick: () -> Unit) {
    BasicImage(
        url = provider.getLogoImage(),
        contentDescription = provider.providerName,
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
        shape = RoundedCornerShape(percent = 10),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.default_padding)
        ),
        border = BorderStroke(1.dp, BlueTake),
        modifier = Modifier
            .height(25.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = BlueTake,
            backgroundColor = BlueTake
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
        PersonImageCircle(imageUrl = castPerson.getProfileImage(), contentDescription = castPerson.name)
        BasicText(
            text = castPerson.name,
            style = MaterialTheme.typography.caption,
            isBold = true
        )
        BasicText(
            text = castPerson.getCharacterName(),
            style = MaterialTheme.typography.caption,
            color = BlueTake
        )
    }
}
