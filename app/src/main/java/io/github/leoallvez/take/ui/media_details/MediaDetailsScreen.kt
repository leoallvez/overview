package io.github.leoallvez.take.ui.media_details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.Background
import io.github.leoallvez.take.ui.theme.BlueTake
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
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
            MediaDetailsContent(mediaDetails = uiState.data, nav) {
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
    nav: NavController,
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
                    nav.navigate(Screen.Home.route)
                }
            }
        ) {
            MediaBody(mediaDetails, nav)
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
            ProvidersList(providers)
            GenreList(genres)
            Overview(overview)
            PersonsList(getOrderedCast(), nav)
            MediaItemList(similar.results, nav)
        }
    }
}

@Composable
fun ReleaseYearAndRunTime(releaseYear: String, runtime: String) {
    Row(Modifier.padding(vertical = 10.dp)) {
        val showSeparator = releaseYear.isNotEmpty().and(runtime.isNotEmpty())
        SimpleSubtitle(releaseYear)
        SimpleSubtitle(" • ", display = showSeparator)
        SimpleSubtitle(runtime)
    }
}

@Composable
fun SimpleSubtitle(subtitle: String, display: Boolean = true) {
    if (subtitle.isNotEmpty() && display) {
        Text(
            text = subtitle,
            color = Color.White,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun ProvidersList(providers: List<ProviderPlace>) {
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
                ProviderItem(provider)
            }
        }
    }
}

@Composable
fun ProviderItem(provider: ProviderPlace) {
    BasicImage(
        url = provider.logoPath(),
        contentDescription = provider.providerName,
        modifier = Modifier
            .size(50.dp)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(dimensionResource(R.dimen.corner)))
    )
}

@Composable
fun GenreList(genres: List<Genre>) {
    if (genres.isNotEmpty()) {
        LazyRow(
            Modifier.padding(
                vertical = dimensionResource(R.dimen.default_padding)
            ),
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.default_padding))
        ) {
            items(genres) { genre ->
                GenreItem(name = genre.name)
            }
        }
    }
}

@Composable
fun GenreItem(name: String) {
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(percent = 10),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.default_padding)
        ),
        border = BorderStroke(1.dp, BlueTake),
        modifier = Modifier
            .height(25.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = BlueTake,
            backgroundColor = Background,
        )
    ) {
        Text(text = name, color = BlueTake, style = MaterialTheme.typography.caption)
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
fun PersonsList(persons: List<Person>, nav: NavController) {
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
                        nav.navigate(route = Screen.CastPerson.editRoute(person.id))
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
                .border(1.dp, Color.DarkGray, CircleShape)
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

@Composable
fun MediaItemList(medias: List<MediaItem>, nav: NavController) {
    if (medias.isNotEmpty()) {
        Column {
            BasicTitle(title = stringResource(R.string.related))
            LazyRow (
                Modifier.padding(
                    vertical = dimensionResource(R.dimen.default_padding)
                ),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(medias) { media ->
                    MediaItem(media) {
                        nav.navigate(
                            Screen.MediaDetails.editRoute(id = media.apiId, type = media.type)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItem(mediaItem: MediaItem, onClick: () -> Unit) {
    Column(Modifier.clickable { onClick.invoke() }) {
        BasicImage(
            url = mediaItem.getItemPoster(),
            contentDescription = mediaItem.getItemTitle()
        )
        BasicText(
            text = mediaItem.getItemTitle(),
            style = MaterialTheme.typography.body1,
            isBold = true,
        )
    }
}

@Composable
fun BasicImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    height: Dp = dimensionResource(R.dimen.image_height),
    contentScale: ContentScale = ContentScale.FillHeight
) {
    if (url.isNotEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = url)
                .crossfade(true)
                .build(),
            modifier = modifier
                .background(Background)
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner))),
            contentScale = contentScale,
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun BasicText(
    text: String,
    style: TextStyle,
    color: Color = Color.White,
    isBold: Boolean = false
) {
    Text(
        color = color,
        text = text,
        modifier = Modifier
            .padding(top = 3.dp)
            .width(dimensionResource(R.dimen.person_profiler_width)),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        fontWeight = if(isBold) FontWeight.Bold else FontWeight.Normal,
        style = style,
    )
}

