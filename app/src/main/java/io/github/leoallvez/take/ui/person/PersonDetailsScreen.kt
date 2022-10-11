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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.leoallvez.take.Logger
import io.github.leoallvez.take.R
import io.github.leoallvez.take.ui.MediaItemList
import io.github.leoallvez.take.data.api.response.PersonResponse as Person
import io.github.leoallvez.take.ui.Screen
import io.github.leoallvez.take.ui.TrackScreenView
import io.github.leoallvez.take.ui.UiStateResult
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

    UiStateResult(
        uiState = viewModel.uiState.collectAsState().value,
        onRefresh = { viewModel.refresh(apiId) }
    ) { dataResult ->
        PersonDetailsContent(dataResult, showAds, onNavigateToHome, onNavigateToMediaDetails) {
            viewModel.refresh(apiId)
        }
    }
}

@Composable
fun PersonDetailsContent(
    person: Person?,
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
            Text(text = "${person?.biography}", modifier = Modifier.align(Alignment.Center))
        }

        MediaItemList(
            listTitle = stringResource(R.string.related),
            items = person?.movieCredits?.crew ?: listOf(),
        ) { apiId, _ ->
            onNavigateToMediaDetails.invoke(apiId, "movie")
        }
        MediaItemList(
            listTitle = stringResource(R.string.related),
            items = person?.tvCredits?.crew ?: listOf(),
        ) { apiId, _ ->
            onNavigateToMediaDetails.invoke(apiId, "tv")
        }
    }
}