package br.dev.singular.overview.presentation.ui.components.episodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.EpisodesProgressUiModel
import br.dev.singular.overview.presentation.model.EpisodesSummaryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal const val SKELETON_SHORT_LINE_FRACTION = 0.7f
internal const val BADGE_CONTAINER_ALPHA = 0.12f

@Composable
internal fun rememberEpisodesProgressViewModel(): IEpisodesProgressViewModel =
    if (LocalInspectionMode.current) {
        remember {
            PreviewEpisodesProgressViewModel(MutableStateFlow(EpisodesProgressUiModel.NotStarted))
        }
    } else {
        hiltViewModel<EpisodesProgressViewModel>()
    }

internal val EpisodesProgressUiModel.watched: EpisodesProgressUiModel.InProgress?
    get() = when (this) {
        is EpisodesProgressUiModel.InProgress -> this
        EpisodesProgressUiModel.Completed -> EpisodesProgressUiModel.InProgress(progress = 1f)
        else -> null
    }

@Composable
internal fun seasonsAndEpisodes(model: EpisodesSummaryUiModel): String = with(model) {
    stringResource(
        R.string.separator,
        pluralStringResource(R.plurals.seasons, numberOfSeasons, numberOfSeasons),
        pluralStringResource(R.plurals.episodes, numberOfEpisodes, numberOfEpisodes)
    )
}

internal class PreviewEpisodesProgressViewModel(
    override val uiState: StateFlow<EpisodesProgressUiModel>,
) : IEpisodesProgressViewModel

internal val previewProgressStates = listOf(
    EpisodesProgressUiModel.Loading,
    EpisodesProgressUiModel.NotStarted,
    EpisodesProgressUiModel.SignedOut,
    EpisodesProgressUiModel.InProgress(progress = 0.08f),
    EpisodesProgressUiModel.Completed,
)
