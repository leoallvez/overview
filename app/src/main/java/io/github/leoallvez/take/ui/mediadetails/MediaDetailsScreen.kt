package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.ui.*
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
        mediaDetails.apply {
            Column {
                AppBar(mediaDetails = mediaDetails, callback = callback)
                Text(
                    text = "$originalTitle (${mediaDetails.releaseDate.slice(0..3)})",
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun AppBar(mediaDetails: MediaDetails?, callback: () -> Unit) {
    Box {
        CardImage(
            data = mediaDetails?.getItemBackdrop(),
            contentDescription = mediaDetails?.originalTitle
        )
        ButtonOutlined(
            callback = callback,
            modifier = Modifier.padding(start = 15.dp, top = 5.dp)
        ) {
            AppIcon(
                Icons.Filled.ArrowBack,
                descriptionResource = R.string.back_to_icon
            )
        }
    }
}
