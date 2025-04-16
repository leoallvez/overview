package br.dev.singular.overview.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.ContentType
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.presentation.ui.theme.PrimaryBackground

@Composable
fun MediaPoster(
    media: MediaUIModel,
    onClick: () -> Unit
) {
    val posterWidth = dimensionResource(R.dimen.poster_width)
    val posterHeight = dimensionResource(R.dimen.poster_height)

    val a11yDescription = when (media.contentType) {
        ContentType.MOVIE -> {
            stringResource(R.string.movie_poster_description, media.title)
        }
        ContentType.TV_SHOW -> {
            stringResource(R.string.tv_show_poster_description, media.title)
        }
    }

    Column(
        Modifier
            .background(PrimaryBackground)
            .clickable { onClick.invoke() }
            .semantics(mergeDescendants = true) {}
    ) {
        BasicImage(
            url = media.posterURLPath,
            withBorder = true,
            modifier = Modifier.size(posterWidth, posterHeight)
        )
        BasicText(
            text = media.title,
            modifier = Modifier
                .width(posterWidth)
                .semantics {
                    contentDescription = a11yDescription
                },
            style = MaterialTheme.typography.bodySmall,
            isBold = true
        )
    }
}

@Preview
@Composable
fun MediaPosterPreview() {
    MediaPoster(
        MediaUIModel(
            id = 1,
            title = "Matrix",
            posterURLPath = "https://imagens.com/movie.jpg",
            contentType = ContentType.MOVIE
        )
    ) { }
}
