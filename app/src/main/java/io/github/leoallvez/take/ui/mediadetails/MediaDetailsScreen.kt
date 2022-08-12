package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    if(mediaDetails == null) {
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
        mediaDetails.apply {
            BackdropImage(
                data = getMediaDetailsBackdrop(),
                contentDescription = originalTitle
            )
            BackdropTitle(text = getMediaDetailsLetter(), modifier = Modifier.align(Alignment.BottomCenter))
        }
        ButtonOutlined(
            callback = backButtonAction,
            modifier = Modifier.padding(5.dp)
        ) {
            AppIcon(
                Icons.Filled.ArrowBack,
                descriptionResource = R.string.back_to_icon
            )
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
    ) {
        GenreList(genres = mediaDetails.genres)
        MediaOverview(overview = mediaDetails.overview)
    }
}

@Composable
fun MediaOverview(overview: String) {
    Text(
        text = overview,
        color = Color.White,
        fontSize = 15.sp,
        textAlign = TextAlign.Justify,
        modifier = Modifier.padding(5.dp)
    )
}

@Composable
fun GenreList(genres: List<Genre>) {
    if (genres.isNotEmpty()) {
        LazyRow(Modifier.padding(vertical = 5.dp)) {
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
        shape = RoundedCornerShape(percent = 100),
        contentPadding = PaddingValues(horizontal = 5.dp),
        border = BorderStroke(1.dp, BlueTake),
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .height(17.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = BlueTake,
            backgroundColor = Color.Black,
        )
    ) {
        Text(text = name, color = BlueTake, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun GenreListPreview() {
    GenreList(genres = mutableListOf("A", "B").map { Genre(name = "Genre $it") })
}


