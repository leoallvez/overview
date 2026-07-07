package br.dev.singular.overview.presentation.ui.screens.media.movie

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.screens.media.MediaDetailsBody
import br.dev.singular.overview.presentation.ui.screens.media.MediaDetailsToolBar
import br.dev.singular.overview.presentation.ui.screens.media.MediaUiStateResult
import br.dev.singular.overview.presentation.ui.screens.media.components.UiMediaInfoItem
import br.dev.singular.overview.presentation.ui.screens.media.movie.interaction.MovieDetailsActions
import br.dev.singular.overview.presentation.ui.screens.media.movie.interaction.rememberMovieDetailsActions
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMovieDetails

/**
 * The entry point for the Movie Details screen.
 * It handles the different UI states (Loading, Success, Error) and displays the movie content.
 *
 * @param movieId The unique identifier of the movie.
 * @param uiState The current state of the UI, providing the [MediaDetailsUiModel.Movie] on success.
 * @param showAds Whether to show ads in this screen.
 * @param actions The actions and intents that can be performed on this screen.
 */
@Composable
fun MovieDetailsScreen(
    movieId: Long,
    uiState: UiState<MediaDetailsUiModel.Movie?>,
    showAds: Boolean = false,
    actions: MovieDetailsActions,
) {
    MediaUiStateResult(
        id = movieId,
        tagPath = actions.tagPath,
        onLoad = actions::onLoad,
        uiState = uiState,
    ) { movie ->
        MediaDetailsBody(
            showAds = showAds,
            model = movie,
            actions = actions,
            onSelectCatalog = actions::onSelectCatalog,
            header = {
                MediaDetailsToolBar(
                    model = movie.metadata,
                    onBack = actions::onBack,
                ) { actions.onLike(movie) }
            },
            infoSlot = {
                UiMediaInfoItem(
                    label = stringResource(R.string.release_date),
                    value = movie.metadata.releaseDate
                )
                UiMediaInfoItem(
                    label = stringResource(R.string.runtime),
                    value = movie.durationFormatted
                )
                UiMediaInfoItem(
                    label = stringResource(R.string.director),
                    value = movie.directors.joinToString(),
                    color = HighlightColor
                )
            }
        )
    }
}

@UiScreenPreview
@Composable
internal fun MovieDetailsScreenPreview() {
    MovieDetailsScreen(
        movieId = 1L,
        showAds = true,
        uiState = UiState.Success(data = fakeMovieDetails()),
        actions = rememberMovieDetailsActions()
    )
}

@UiScreenPreview
@Composable
internal fun MovieDetailsScreenLoadingPreview() {
    MovieDetailsScreen(
        movieId = 1L,
        uiState = UiState.Loading(),
        actions = rememberMovieDetailsActions()
    )
}

@UiScreenPreview
@Composable
internal fun MovieDetailsScreenErrorPreview() {
    MovieDetailsScreen(
        movieId = 1L,
        uiState = UiState.Error(),
        actions = rememberMovieDetailsActions()
    )
}
