package io.github.leoallvez.take.ui.mediadetails

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.Screen
import io.github.leoallvez.take.ui.TrackScreenView

@Composable
fun MediaDetailsScreen(id: Long?, type: String?, logger: Logger) {

    TrackScreenView(screen = Screen.MediaDetails, logger)

    Text(text = "Clicked on apiId: $id, type: $type", color = Color.Black)
}