package io.github.leoallvez.take.ui.media_details

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
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.api.response.Genre
import io.github.leoallvez.take.data.api.response.ProviderPlace
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.navigation.MediaDetailsScreenEvents
import io.github.leoallvez.take.ui.theme.BlueTake
import io.github.leoallvez.take.ui.theme.PrimaryBackground
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import io.github.leoallvez.take.data.api.response.MediaDetailResponse as MediaDetails
import io.github.leoallvez.take.data.api.response.PersonResponse as Person

@Composable
fun MediaDetailsScreen(
    logger: Logger,
    params: Pair<Long, String>,
    events: MediaDetailsScreenEvents,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {

    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value
    TrackScreenView(screen = Screen.MediaDetails, logger)

    val (apiId: Long, mediaType: String) = params
    viewModel.loadMediaDetails(apiId, mediaType)

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId, mediaType) }
    ) { dataResult ->
        MediaDetailsContent(dataResult, showAds, events) {
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
                    .padding(12.dp)
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
            .padding(dimensionResource(R.dimen.default_padding)),
    ) {
        mediaDetails.apply {
            ScreenTitle(getLetter())
            ReleaseYearAndRunTime(getReleaseYear(), getRuntimeFormatted())
            ProvidersList(providers) { apiId ->
                events.onNavigateToDiscover(apiId = apiId)
            }
            GenreList(genres) { apiId ->
                events.onNavigateToCastDetails(apiId = apiId)
            }
            BasicParagraph(R.string.synopsis, overview)
            AdsBanner(
                bannerId = R.string.banner_sample_id,
                isVisible = showAds,
            )
            CastList(getOrderedCast()) { apiId ->
                events.onNavigateToCastDetails(apiId = apiId)
            }
            MediaItemList(
                listTitle = stringResource(R.string.related),
                items = similar.results,
            ) { apiId, mediaType ->
                events.onNavigateToMediaDetails(apiId = apiId, mediaType = mediaType)
            }
        }
    }
}

@Composable
fun ReleaseYearAndRunTime(releaseYear: String, runtime: String) {
    Row(Modifier.padding(vertical = 10.dp)) {
        val spacerModifier = Modifier.padding(horizontal = 2.dp)
        SimpleSubtitle(text = releaseYear)
        Spacer(modifier = spacerModifier)
        SimpleSubtitle(
            text = stringResource(R.string.separator),
            display = releaseYear.isNotEmpty().and(runtime.isNotEmpty())
        )
        Spacer(modifier = spacerModifier)
        SimpleSubtitle(text = runtime)
    }
}

@Composable
fun ProvidersList(providers: List<ProviderPlace>, onClickItem: (Long) -> Unit) {
    if (providers.isNotEmpty()) {
        BasicTitle(stringResource(R.string.where_to_watch))
        LazyRow (
            Modifier.padding(
                vertical = 10.dp
            ),
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.default_padding))
        ) {
            items(providers) { provider ->
                ProviderItem(provider) {
                    onClickItem.invoke(provider.apiId)
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
fun GenreList(genres: List<Genre>, onClickItem: (Long) -> Unit) {
    if (genres.isNotEmpty()) {
        LazyRow(
            Modifier.padding(
                vertical = dimensionResource(R.dimen.default_padding)
            ),
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.default_padding))
        ) {
            items(genres) { genre ->
                GenreItem(name = genre.name) {
                    onClickItem.invoke(genre.apiId)
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
            backgroundColor = BlueTake,
        ),
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
            LazyRow (
                Modifier.padding(
                    vertical = dimensionResource(R.dimen.default_padding)
                ),
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
            style =  MaterialTheme.typography.caption,
            isBold = true,
        )
        BasicText(
            text = castPerson.character,
            style = MaterialTheme.typography.caption,
            color = BlueTake,
        )
    }
}
