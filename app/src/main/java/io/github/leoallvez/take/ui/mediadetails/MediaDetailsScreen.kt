package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
fun MediaBody(mediaDetails: MediaDetails) {
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

@Composable
fun PersonsList(persons: List<Person>) {
    if (persons.isNotEmpty()) {
        Column {
            BasicTitle(title = stringResource(R.string.cast))
            LazyRow (
                Modifier.padding(
                    vertical = dimensionResource(R.dimen.default_padding)
                ),
                horizontalArrangement = Arrangement
                    .spacedBy(5.dp)
            ) {
                items(persons) { person ->
                    PersonItem(person = person)
                }
            }
        }
    }
}

@Composable 
fun PersonItem(person: Person) {
    Column {
        BasicImage(url = person.getProfile(), contentDescription = person.name)
        PersonText(
            text = person.name,
            style = MaterialTheme.typography.subtitle1,
            isBold = true,
        )
        PersonText(
            text = person.character,
            style = MaterialTheme.typography.caption,
            color = BlueTake,
        )
    }
}

@Composable
fun BasicImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    height: Dp = dimensionResource(R.dimen.image_height),
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
            contentScale = ContentScale.FillHeight,
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun PersonText(
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
            backgroundColor = Background,
        )
    ) {
        Text(text = name, color = BlueTake, style = MaterialTheme.typography.caption)
    }
}
