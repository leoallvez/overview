package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.data.api.response.MediaDetailResponse as MediaDetails

@Composable
fun MediaDetailsScreen(
    params: Pair<Long,String>,
    viewModel: MediaDetailsViewModel = hiltViewModel(),
    logger: Logger
) {
    val (apiId: Long, type: String) = params
    TrackScreenView(screen = Screen.MediaDetails, logger)
    viewModel.loadMediaDetails(id = apiId, type = type)

    when(val uiState = viewModel.uiState.collectAsState().value) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Success -> MediaDetailsContent(mediaDetails = uiState.data)
        is UiState.Error   -> ErrorOnLoading(refresh = {
            viewModel.loadMediaDetails(id = apiId, type = type)
        })
    }
}

@Composable
fun MediaDetailsContent(mediaDetails: MediaDetails?) {

    Text(text = "Media content: $mediaDetails", color = Color.Black)
}