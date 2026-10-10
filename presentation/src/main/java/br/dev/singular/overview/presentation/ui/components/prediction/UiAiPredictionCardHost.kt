package br.dev.singular.overview.presentation.ui.components.prediction

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMovieDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun UiAiPredictionCardHost(
    media: MediaDetailsUiModel,
    modifier: Modifier = Modifier,
    onPredict: (isTryAgain: Boolean) -> Unit = {},
    onLogin: () -> Unit = {},
    viewModel: IAiPredictionViewModel = rememberAiPredictionViewModel(),
) {
    val prediction by viewModel.uiState.collectAsState()

    AnimatedContent(
        targetState = prediction,
        modifier = modifier,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentKey = { it::class },
        label = "UiAiPredictionCardHost",
    ) { model ->
        UiAiPredictionCard(
            model = model,
            onLogin = onLogin,
            onPredict = {
                onPredict(it)
                viewModel.onPredict(media)
            },
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiAiPredictionCardHostPreview() {

    val states = remember { MutableStateFlow(previewStates.first()) }

    val viewModel = remember(states) { PreviewAiPredictionViewModel(states) }

    LaunchedEffect(states) {
        while (true) {
            previewStates.forEach { state ->
                states.value = state
                delay(2.seconds)
            }
        }
    }

    Box(modifier = Modifier.size(width = 360.dp, height = 200.dp)) {
        UiAiPredictionCardHost(
            media = fakeMovieDetails(),
            viewModel = viewModel,
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        )
    }
}
