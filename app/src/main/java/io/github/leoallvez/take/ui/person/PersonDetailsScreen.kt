package io.github.leoallvez.take.ui.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.ui.*
import io.github.leoallvez.take.ui.theme.Background
import io.github.leoallvez.take.util.MediaItemClick

@Composable
fun CastDetailsScreen(
    apiId: Long,
    logger: Logger,
    onNavigateToHome: () -> Unit,
    onNavigateToMediaDetails: MediaItemClick,
    viewModel: PersonDetailsViewModel = hiltViewModel(),
) {
    val showAds = viewModel.adsAreVisible().observeAsState(initial = false).value

    TrackScreenView(screen = Screen.CastDetails, logger)

    viewModel.loadPersonDetails(apiId)

    when(val uiState = viewModel.uiState.collectAsState().value) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Success -> {
            PersonDetailsContent(uiState.data, showAds, onNavigateToHome, onNavigateToMediaDetails) {
                viewModel.refresh(apiId)
            }
        }
        is UiState.Error -> ErrorOnLoading { viewModel.refresh(apiId) }
    }
}

@Composable
fun PersonDetailsContent(
    personDetails: PersonResponse?,
    showAds: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToMediaDetails: MediaItemClick,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Background)
            .fillMaxSize()
    ) {
        Box {
            Text(text = "${personDetails?.toString()}", modifier = Modifier.align(Alignment.Center))
        }
    }
}