package io.github.leoallvez.take.ui.media_details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.api.response.Genre
import io.github.leoallvez.take.data.api.response.Person
import io.github.leoallvez.take.data.api.response.ProviderPlace
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.Background
import io.github.leoallvez.take.ui.theme.BlueTake
import io.github.leoallvez.take.ui.theme.BorderColor
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import timber.log.Timber
import io.github.leoallvez.take.data.api.response.MediaDetailResponse as MediaDetails

@Composable
fun MediaDetailsScreen(
    nav: NavController,
    logger: Logger,
    params: Pair<Long, String>,
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    TrackScreenView(screen = Screen.MediaDetails, logger)

    val (apiId: Long, mediaType: String) = params
    viewModel.loadMediaDetails(apiId, mediaType)

    when(val uiState = viewModel.uiState.collectAsState().value) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Success -> {
            MediaDetailsContent(mediaDetails = uiState.data, navigation = nav) {
                viewModel.refresh(apiId, mediaType)
            }
        }
        is UiState.Error -> {
            ErrorOnLoading {
                viewModel.refresh(apiId, mediaType)
            }
        }
    }
}

@Composable
fun MediaDetailsContent(
    mediaDetails: MediaDetails?,
    navigation: NavController,
    refresh: () -> Unit
) {
    if (mediaDetails == null) {
        ErrorOnLoading { refresh.invoke() }
    } else {
        CollapsingToolbarScaffold(
            modifier = Modifier,
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                MediaToolBar(mediaDetails) {
                    navigation.navigate(Screen.Home.route)
                }
            }
        ) {
            MediaBody(mediaDetails, navigation)
        }
    }
}

@Composable
fun MediaToolBar(mediaDetails: MediaDetails, backButtonAction: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        mediaDetails.apply {
            MediaBackdrop(
                url = getBackdrop(),
                contentDescription = originalTitle,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
            BasicImage(
                url = getPoster(),
                contentDescription = originalTitle,
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
fun MediaBackdrop(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = url)
            .crossfade(true)
            .build(),
        modifier = modifier
            .background(Background)
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner))),
        contentScale = ContentScale.FillHeight,
        contentDescription = contentDescription,
    )
}

@Composable
fun MediaBody(
    mediaDetails: MediaDetails,
    nav: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .verticalScroll(rememberScrollState())
            .background(Background)
            .padding(
                vertical = dimensionResource(R.dimen.default_padding),
                horizontal = dimensionResource(R.dimen.screen_padding),
            ),
    ) {
        mediaDetails.apply {
            ScreenTitle(getMediaDetailsLetter())
            ReleaseYearAndRunTime(getReleaseYear(), getRuntimeFormatted())
            ProvidersList(providers, navigation = nav)
            GenreList(genres, navigation = nav)
            Overview(overview)
            PersonsList(getOrderedCast(), navigation = nav)
            MediaItemList(similar.results, navigation = nav)
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
fun ProvidersList(providers: List<ProviderPlace>, navigation: NavController) {
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
                    navigation.navigate(Screen.Discover.editRoute(provider.id))
                }
            }
        }
    }
}

@Composable
fun ProviderItem(provider: ProviderPlace, onClick: () -> Unit) {
    BasicImage(
        url = provider.logoPath(),
        contentDescription = provider.providerName,
        modifier = Modifier
            .size(50.dp)
            .border(
                dimensionResource(R.dimen.border_width),
                BorderColor,
                RoundedCornerShape(dimensionResource(R.dimen.corner))
            )
            .clickable { onClick.invoke() }
    )
}

@Composable
fun GenreList(genres: List<Genre>, navigation: NavController) {
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
                    Timber.tag("click_example").i("click in genre!")
                    navigation.navigate(Screen.Discover.editRoute(genre.id))
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
            color = Background,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Overview(overview: String) {
    if (overview.isNotBlank()) {
        Column {
            BasicTitle(stringResource(R.string.synopsis))
            Text(
                text = overview,
                color = Color.White,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun PersonsList(persons: List<Person>, navigation: NavController) {
    if (persons.isNotEmpty()) {
        Column {
            BasicTitle(title = stringResource(R.string.cast))
            LazyRow (
                Modifier.padding(
                    vertical = dimensionResource(R.dimen.default_padding)
                ),
            ) {
                items(persons) { person ->
                    PersonItem(person = person) {
                        navigation.navigate(route = Screen.CastPerson.editRoute(person.id))
                    }
                }
            }
        }
    }
}

@Composable 
fun PersonItem(person: Person, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        BasicImage(
            url = person.getProfile(),
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(dimensionResource(R.dimen.border_width), BorderColor, CircleShape),
            placeholder = painterResource(R.drawable.avatar),
            errorDefaultImage = painterResource(R.drawable.avatar)
        )
        BasicText(
            text = person.name,
            style =  MaterialTheme.typography.caption,
            isBold = true,
        )
        BasicText(
            text = person.character,
            style = MaterialTheme.typography.caption,
            color = BlueTake,
        )
    }
}
