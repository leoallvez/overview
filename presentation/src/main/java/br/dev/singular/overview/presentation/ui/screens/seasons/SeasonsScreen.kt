package br.dev.singular.overview.presentation.ui.screens.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.SeasonUiModel
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.UiDivider
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.episodes.item.UiEpisodeItem
import br.dev.singular.overview.presentation.ui.components.episodes.item.UiEpisodeItemSkeleton
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.progress.UiProgressBar
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHostState
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import br.dev.singular.overview.presentation.ui.screens.seasons.interaction.SeasonsActions
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.SubtitleTextColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeSeasonsScreenUiModel
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection

@Composable
fun SeasonsScreen(
    tvShowId: Long,
    uiState: UiState<SeasonUiModel>,
    actions: SeasonsActions,
) {
    LaunchedEffect(tvShowId) {
        actions.onLoad(tvShowId)
    }

    UiStateResult(
        uiState = uiState,
        tagPath = actions.tagPath,
        onRefresh = actions::onRefresh,
        loadingContent = {
            SeasonsSkeletonScreen(tagPath = actions.tagPath)
        }
    ) { season ->
        SeasonsContent(season, actions)
    }
}

@Composable
internal fun SeasonsContent(
    season: SeasonUiModel,
    actions: SeasonsActions
) {
    val snackbarHostState = UiSnackbarHostState()

    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    // Simulate snackbar for preview purposes based on screenshot
    LaunchedEffect(Unit) {
        snackbarHostState.show(
            UiSnackbarVisuals.Close(
                title = "E5 watched",
                message = "5 of 10 in season 1",
                style = UiSnackbarStyle.Success
            )
        )
    }

    UiScaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            UiTopAppBar(
                title = stringResource(R.string.episodes_title),
                subtitle = season.tvShowTitle,
                onBack = actions::onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            UiSeasonSelector(
                season.numberOfSeasons
            ) { seasonNumber ->
                actions.onSeasonSelected(seasonNumber)
            }
            UiSeasonProgress(
                visible = !isCollapsed,
                progressText = season.progressText,
                progressPercentage = season.progressPercentage,
                onMarkSeason = actions::onMarkSeason
            )
            UiDivider(visible = isCollapsed)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.spacing_20x))
            ) {
                if (season.isEpisodesLoading) {
                    items(15) {
                        UiEpisodeItemSkeleton(
                            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_2x))
                        )
                    }
                } else {
                    items(season.episodes, key = { it.episodeNumber }) { episode ->
                        UiEpisodeItem(
                            model = episode,
                            isWatched = season.watchedEpisodes.contains(episode.episodeNumber),
                            modifier = Modifier
                                .padding(vertical = dimensionResource(R.dimen.spacing_1x)),
                            onClick = { actions.onToggleWatched(episode.episodeNumber) },
                            onToggleWatched = { actions.onToggleWatched(episode.episodeNumber) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun UiSeasonSelector(
    numberOfSeasons: Int,
    onClick: (seasonNumber: Int) -> Unit
) {
    var selectedSeason by remember { mutableIntStateOf(1) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacing_2x)),
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        items(
            count = numberOfSeasons,
            key = { index -> index }
        ) { index ->
            val seasonNumber = index + 1
            UiChip(
                text = stringResource(R.string.season_number, seasonNumber),
                activated = selectedSeason == seasonNumber,
                onClick = {
                    selectedSeason = seasonNumber
                    onClick.invoke(seasonNumber)
                }
            )
        }
    }
}

@Composable
internal fun UiSeasonProgress(
    visible: Boolean,
    progressText: String,
    progressPercentage: Float,
    onMarkSeason: () -> Unit
) {
    UiAnimatedVisibility(visible = visible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.spacing_2x))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UiText(
                    text = progressText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleTextColor
                )
                TextButton(
                    onClick = onMarkSeason
                ) {
                    UiText(
                        text = stringResource(R.string.mark_season),
                        style = MaterialTheme.typography.titleSmall,
                        color = HighlightColor,
                        isBold = true
                    )
                }
            }

            UiProgressBar(
                progress = progressPercentage,
                heightRes = R.dimen.spacing_1x
            )
        }
    }
}

@UiScreenPreview
@Composable
internal fun SeasonsScreenPreview() {
    SeasonsScreen(
        tvShowId = 1L,
        uiState = UiState.Success(fakeSeasonsScreenUiModel()),
        actions = SeasonsActions()
    )
}

@UiScreenPreview
@Composable
internal fun SeasonsScreenLoadingSeasonsPreview() {
    SeasonsScreen(
        tvShowId = 1L,
        uiState = UiState.Success(fakeSeasonsScreenUiModel(isEpisodesLoading = true)),
        actions = SeasonsActions()
    )
}
