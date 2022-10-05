package io.github.leoallvez.take.ui.cast_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.ui.Screen
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.ui.theme.Background
import timber.log.Timber

@Composable
fun CastDetailsScreen(
    apiId: Long?,
    nav: NavController,
    logger: Logger,
    viewModel: CastDetailsViewModel = hiltViewModel(),
) {
    TrackScreenView(screen = Screen.CastDetails, logger)

    Column(
        modifier = Modifier
            .background(Background)
            .fillMaxSize()
    ) {
        Box {
            Text(text = "Cast person screen id: $apiId", modifier = Modifier.align(Alignment.Center))
        }
    }
}