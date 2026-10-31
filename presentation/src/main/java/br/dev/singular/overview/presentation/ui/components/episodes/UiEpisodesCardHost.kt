package br.dev.singular.overview.presentation.ui.components.episodes

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
import br.dev.singular.overview.presentation.model.EpisodesSummaryUiModel
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeEpisodesSummary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds

/**
 * Connects [UiEpisodesCard] to the user's watching progress, animating between its states.
 *
 * @param model The [EpisodesSummaryUiModel] of the TV show whose episodes the card leads to.
 * @param modifier The modifier to be applied to this component.
 * @param onClick Callback invoked when the card is clicked.
 * @param viewModel The [IEpisodesProgressViewModel] providing the user's progress.
 */
@Composable
internal fun UiEpisodesCardHost(
    model: EpisodesSummaryUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    viewModel: IEpisodesProgressViewModel = rememberEpisodesProgressViewModel(),
) {
    val progress by viewModel.uiState.collectAsState()

    AnimatedContent(
        targetState = progress,
        modifier = modifier,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentKey = { it::class },
        label = "UiEpisodesCardHost",
    ) { state ->
        UiEpisodesCard(
            model = model,
            progress = state,
            onClick = onClick,
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardHostPreview() {

    val states = remember { MutableStateFlow(previewProgressStates.first()) }

    val viewModel = remember(states) { PreviewEpisodesProgressViewModel(states) }

    LaunchedEffect(states) {
        while (true) {
            previewProgressStates.forEach { state ->
                states.value = state
                delay(2.seconds)
            }
        }
    }

    Box(modifier = Modifier.size(width = 360.dp, height = 140.dp)) {
        UiEpisodesCardHost(
            model = fakeEpisodesSummary(),
            viewModel = viewModel,
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        )
    }
}
