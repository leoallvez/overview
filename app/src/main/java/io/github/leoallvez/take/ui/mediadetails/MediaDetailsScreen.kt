package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.Screen
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.ui.home.HomeViewModel

@Composable
fun MediaDetailsScreen(
    params: Pair<Long,String>,
    viewModel: MediaDetailsViewModel = hiltViewModel(),
    logger: Logger
) {
    viewModel.getMediaDetails(id = params.first, type = params.second)
    TrackScreenView(screen = Screen.MediaDetails, logger)

    Text(text = "Clicked on apiId: ${params.first}, type: ${params.second}", color = Color.Black)
}