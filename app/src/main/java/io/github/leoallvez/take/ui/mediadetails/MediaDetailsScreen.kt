package io.github.leoallvez.take.ui.mediadetails

import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.data.api.response.Genre
import io.github.leoallvez.take.ui.*
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
    Box {
        BackdropImage(
            data = mediaDetails.getMediaDetailsBackdrop(),
            contentDescription = mediaDetails.originalTitle
        )
        ToolbarButton(
            painter = Icons.Default.KeyboardArrowLeft,
            descriptionResource = R.string.back_to_home_icon
        ) { backButtonAction.invoke() }
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
            .padding(15.dp)
    ) {
        mediaDetails.apply {
            Row(
                //verticalAlignment = Alignment.Bottom
            ) {
                ScreenTitle(getMediaDetailsLetter())
                ReleaseYear(releaseYear())
            }
            GenreList(genres)
            ScreenSubtitle(tagline)
            BodyText(overview)
        }
    }
}

@Composable
fun ReleaseYear(date: String) {
    if(date.isNotEmpty()) {
        Text(
            text = " $date ",
            style = MaterialTheme.typography.h6,
            color = Color.White,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(bottom = 15.dp)
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


