package br.dev.singular.overview.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.presentation.ui.theme.PrimaryBackground

@Composable
fun MediaPoster(media: MediaUIModel, onClick: () -> Unit) {
    Column(
        Modifier
            .background(PrimaryBackground)
            .clickable { onClick.invoke() }
    ) {
        BasicImage(
            url = media.posterPath,
            contentDescription = media.letter,
            withBorder = true,
            modifier = Modifier.height(200.dp)
        )
        BasicText(
            text = media.letter,
            modifier = Modifier.width(130.dp),
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
            letter = "Matrix",
            posterPath = "https://imagens.com/movie.jpg",
            type = "Movie"
        )
    ) { }
}