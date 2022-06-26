package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.runtime.Composable
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.Screen
import io.github.leoallvez.take.ui.TrackScreenView

@Composable
fun MediaDetailsScreen(logger: Logger) {

    TrackScreenView(screen = Screen.MediaDetails, logger)

}