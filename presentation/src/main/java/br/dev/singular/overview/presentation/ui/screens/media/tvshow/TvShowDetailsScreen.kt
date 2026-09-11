package br.dev.singular.overview.presentation.ui.screens.media.tvshow

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.screens.media.MediaDetailsBody
import br.dev.singular.overview.presentation.ui.screens.media.MediaDetailsToolBar
import br.dev.singular.overview.presentation.ui.screens.media.MediaUiStateResult
import br.dev.singular.overview.presentation.ui.screens.media.components.UiMediaInfoItem
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction.TvShowDetailsActions
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction.rememberTvShowDetailsActions
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeTvShowDetails

/**
 * The entry point for the TV Show Details screen.
 * It handles the different UI states (Loading, Success, Error) and displays the TV show content.
 *
 * @param tvShowId The unique identifier of the TV show.
 * @param uiState The current state of the UI, providing the [MediaDetailsUiModel.TvShow] on success.
 * @param showAds Whether to show ads in this screen.
 * @param actions The actions and intents that can be performed on this screen.
 */
@Composable
fun TvShowDetailsScreen(
    tvShowId: Long,
    uiState: UiState<MediaDetailsUiModel.TvShow?>,
    showAds: Boolean = false,
    actions: TvShowDetailsActions
) {
    MediaUiStateResult(
        id = tvShowId,
        tagPath = actions.tagPath,
        onLoad = actions::onLoad,
        uiState = uiState,
    ) { tvShow ->
        MediaDetailsBody(
            showAds = showAds,
            model = tvShow,
            actions = actions,
            onSelectCatalog = actions::onSelectCatalog,
            header = {
                MediaDetailsToolBar(
                    model = tvShow.metadata,
                    onBack = actions::onBack,
                    onLike = { actions.onLike(tvShow) }
                )
            },
            infoSlot = {
                UiMediaInfoItem(
                    label = stringResource(R.string.release_date),
                    value = tvShow.metadata.releaseDate
                )
                UiMediaInfoItem(
                    label = pluralStringResource(
                        R.plurals.seasons,
                        tvShow.numberOfSeasons,
                        tvShow.numberOfSeasons
                    ),
                    value = pluralStringResource(
                        R.plurals.episodes,
                        tvShow.numberOfEpisodes,
                        tvShow.numberOfEpisodes
                    )
                )
                if (tvShow.runtimePerEpisode.isNotEmpty()) {
                    UiMediaInfoItem(
                        value = stringResource(
                            R.string.runtime_per_episode,
                            tvShow.runtimePerEpisode
                        )
                    )
                }
                UiMediaInfoItem(
                    label = stringResource(R.string.creator),
                    value = tvShow.creators.joinToString(),
                    color = HighlightColor
                )
            }
        )
    }
}

@UiScreenPreview
@Composable
internal fun TvShowDetailsScreenPreview() {
    TvShowDetailsScreen(
        tvShowId = 1L,
        showAds = true,
        uiState = UiState.Success(data = fakeTvShowDetails()),
        actions = rememberTvShowDetailsActions()
    )
}

@UiScreenPreview
@Composable
internal fun TvShowDetailsScreenLoadingPreview() {
    TvShowDetailsScreen(
        tvShowId = 1L,
        uiState = UiState.Loading(),
        actions = rememberTvShowDetailsActions()
    )
}

@UiScreenPreview
@Composable
internal fun TvShowDetailsScreenErrorPreview() {
    TvShowDetailsScreen(
        tvShowId = 1L,
        uiState = UiState.Error(),
        actions = rememberTvShowDetailsActions()
    )
}
