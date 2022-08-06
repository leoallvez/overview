package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.ui.*
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
        CardImage(
            data = mediaDetails.getMediaDetailsBackdrop(),
            contentDescription = mediaDetails.originalTitle
        )
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
        MainTitle(mediaDetails.getMediaDetailsLetter())
        LazyRow {
            items(mediaDetails.genres) { genre ->
                Text(text = " ${genre.name} ")
            }
        }
        Text(
            text = mediaDetails.overview,
            color = Color.White,
            fontSize = 15.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun MainTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        modifier = Modifier
            .padding(
                start = 5.dp,
                top = 10.dp,
                bottom = 10.dp,
            ),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}

