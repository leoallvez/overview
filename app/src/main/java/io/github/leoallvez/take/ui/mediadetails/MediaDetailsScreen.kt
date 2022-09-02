package io.github.leoallvez.take.ui.mediadetails

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.api.response.Genre
import io.github.leoallvez.take.data.api.response.Person
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.BlueTake
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
            MediaDetailsContent(mediaDetails = uiState.data) {
                nav.navigate(Screen.Home.route)
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
    callback: () -> Unit
) {
    if (mediaDetails == null) {
        ErrorOnLoading {}
    } else {
        Timber.tag("take_cast").i("cast: ${mediaDetails.getOrderedCast()}")
        CollapsingToolbarScaffold(
            modifier = Modifier,
            scrollStrategy = ScrollStrategy.EnterAlways,
            state = rememberCollapsingToolbarScaffoldState(),
            toolbar = {
                MediaToolBar(mediaDetails) { callback.invoke() }
            }
        ) {
            MediaBody(mediaDetails)
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
            MediaPoster(
                url = getPoster(),
                contentDescription = originalTitle,
                modifier = Modifier.align(Alignment.BottomStart)
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
            .background(Color.Black)
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(5.dp)),
        contentScale = ContentScale.FillHeight,
        contentDescription = contentDescription,
    )
}

@Composable
fun MediaPoster(url: String, contentDescription: String?, modifier: Modifier = Modifier) {
    if(url.isNotEmpty()) {
        Box(
            modifier = modifier
                .size(width = 140.dp, height = 200.dp)
                .padding(12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(6.dp),
                contentColor = Color.Black,
                elevation = 15.dp,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = url)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .shadow(2.dp, shape = RoundedCornerShape(5.dp))
                )
            }
        }
    }
}


@Composable
fun MediaBody(mediaDetails: MediaDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
            .padding(
                vertical = dimensionResource(R.dimen.default_padding),
                horizontal = dimensionResource(R.dimen.screen_padding),
            ),
    ) {
        mediaDetails.apply {
            Row(Modifier.padding(bottom = 10.dp)) {
                ScreenTitle(getMediaDetailsLetter())
                ReleaseYear(releaseYear())
            }
            GenreList(genres)
            ScreenSubtitle(tagline)
            BodyText(overview)
            PersonsList(getOrderedCast())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonsList(persons: List<Person>) {
    if (persons.isNotEmpty()) {
        LazyRow (
            Modifier.padding(
                vertical = dimensionResource(R.dimen.default_padding)
            ),
            horizontalArrangement = Arrangement
                .spacedBy(10.dp)
        ) {
            items(persons) { person ->
                PersonItem(person = person)
            }
        }
    }
}

@Composable 
fun PersonItem(person: Person) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = person.getProfile())
            .crossfade(true)
            .build(),
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(5.dp)),
        contentScale = ContentScale.FillHeight,
        placeholder = painterResource(R.drawable.placeholder),
        contentDescription = person.name,
    )
}

@Composable
fun ReleaseYear(year: String) {
    if(year.isNotEmpty()) {
        Text(
            text = year,
            style = MaterialTheme.typography.subtitle2,
            color = Color.White,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .fillMaxHeight()
                .padding(dimensionResource(R.dimen.default_padding))
        )
    }
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
            backgroundColor = Color.Black,
        )
    ) {
        Text(text = name, color = BlueTake, style = MaterialTheme.typography.caption)
    }
}

@Preview
@Composable
fun GenreListPreview() {
    GenreList(genres = mutableListOf("A", "B").map { Genre(name = "Genre $it") })
}


