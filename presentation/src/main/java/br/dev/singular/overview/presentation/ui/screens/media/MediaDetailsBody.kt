package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.components.UiAdsMediumRectangle
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogList
import br.dev.singular.overview.presentation.ui.components.genre.UiGenreList
import br.dev.singular.overview.presentation.ui.components.media.UiMediaList
import br.dev.singular.overview.presentation.ui.components.person.UiPersonList
import br.dev.singular.overview.presentation.ui.components.text.UiParagraph
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.components.video.UiVideoList
import br.dev.singular.overview.presentation.ui.screens.media.interaction.MediaDetailsActions
import br.dev.singular.overview.presentation.ui.screens.media.movie.interaction.rememberMovieDetailsActions
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.UiPlaceholder
import br.dev.singular.overview.presentation.ui.utils.fakeMovieDetails

/**
 * A common layout for the body of media details screens (Movies, TV Shows).
 * It organizes common sections like synopsis, cast, crew, similar media, and videos.
 *
 * @param showAds Whether to show ads in the body.
 * @param model The data model containing media details.
 * @param actions The actions and intents for interaction.
 * @param modifier The modifier to be applied to the layout.
 * @param header A composable slot for the screen header (usually including the backdrop).
 * @param infoSlot A composable slot for media-specific information (e.g., duration, seasons).
 */
@Composable
internal fun MediaDetailsBody(
    showAds: Boolean,
    model: MediaDetailsUiModel,
    actions: MediaDetailsActions,
    onSelectCatalog: (CatalogUiModel) -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    infoSlot: @Composable ColumnScope.() -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.spacing_4x)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x)),
        contentPadding = PaddingValues(bottom = horizontalPadding)
    ) {
        item { header() }

        item {
            UiCatalogList(
                catalogs = model.extras.catalogs,
                isReleased = model.metadata.isReleased,
                onClick = onSelectCatalog,
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                content = { infoSlot() }
            )
        }

        if (model.extras.genres.isNotEmpty()) {
            item {
                UiGenreList(
                    genres = model.extras.genres,
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                )
            }
        }

        if (model.metadata.synopsis.isNotEmpty()) {
            item {
                Synopsis(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    synopsis = model.metadata.synopsis
                )
            }
        }

        if (showAds) {
            item {
                UiAdsMediumRectangle(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    prodBannerId = R.string.media_details_banner,
                    isVisible = true
                )
            }
        }

        if (model.extras.videos.isNotEmpty()) {
            item {
                UiVideoList(
                    videos = model.extras.videos,
                    onClick = actions::onToVideoPlayer,
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                )
            }
        }

        if (model.credits.cast.isNotEmpty()) {
            item {
                UiPersonList(
                    title = stringResource(R.string.cast),
                    people = model.credits.cast,
                    onClick = actions::onToPersonDetails,
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                )
            }
        }

        if (model.credits.crew.isNotEmpty()) {
            item {
                UiPersonList(
                    title = stringResource(R.string.crew),
                    people = model.credits.crew,
                    onClick = actions::onToPersonDetails,
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                )
            }
        }

        if (model.extras.similar.isNotEmpty()) {
            item {
                UiMediaList(
                    items = model.extras.similar,
                    onClick = actions::onToMediaDetails,
                    title = stringResource(R.string.related),
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                )
            }
        }
    }
}

@Composable
private fun Synopsis(
    synopsis: String,
    modifier: Modifier = Modifier
) {
    if (synopsis.isNotEmpty()) {
        Column(modifier = modifier) {
            UiTitle(text = stringResource(R.string.synopsis))
            UiParagraph(text = synopsis)
        }
    }
}

@UiComponentPreview
@Composable
private fun MediaDetailsBodyPreview() {
    MediaDetailsBody(
        showAds = true,
        model = fakeMovieDetails(),
        actions = rememberMovieDetailsActions(),
        onSelectCatalog = {},
        header = {
            UiPlaceholder(
                modifier = Modifier.height(250.dp),
                text = "Header Slot"
            )
        },
        infoSlot = {
            UiPlaceholder(text = "Info Slot")
        }
    )
}
