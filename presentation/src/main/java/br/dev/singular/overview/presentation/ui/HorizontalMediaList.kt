package br.dev.singular.overview.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.ContentType
import br.dev.singular.overview.presentation.model.MediaUIModel
import androidx.compose.foundation.lazy.items
import br.dev.singular.overview.presentation.ui.theme.PrimaryBackground

@Composable
fun HorizontalMediaList(
    title: String,
    items: List<MediaUIModel>,
    onClick: (MediaUIModel) -> Unit
) {
    if (items.isNotEmpty()) {
        val spacingXS = dimensionResource(R.dimen.spacing_xs)
        Column(
            modifier = Modifier
                .background(PrimaryBackground)
                .padding(vertical = spacingXS)
        ) {
            SectionTitle(title)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(spacingXS)) {
                items(items) { item ->
                    MediaPoster(item, onClick)
                }
            }
        }
    }
}

@Preview
@Composable
fun HorizontalMediaListPreview() {
    HorizontalMediaList(
        title = "Movies",
        items = listOf(
            MediaUIModel(
                id = 1,
                title = "Matrix",
                posterURLPath = "https://imagens.com/movie.jpg",
                contentType = ContentType.MOVIE,
                previewContent = painterResource(R.drawable.samper_poster_matrix)
            ),
            MediaUIModel(
                id = 1,
                title = "Matrix",
                posterURLPath = "https://imagens.com/movie.jpg",
                contentType = ContentType.MOVIE,
                previewContent = painterResource(R.drawable.samper_poster_matrix)
            ),
            MediaUIModel(
                id = 1,
                title = "Matrix",
                posterURLPath = "https://imagens.com/movie.jpg",
                contentType = ContentType.MOVIE,
                previewContent = painterResource(R.drawable.samper_poster_matrix)
            ),
            MediaUIModel(
                id = 1,
                title = "Matrix",
                posterURLPath = "https://imagens.com/movie.jpg",
                contentType = ContentType.MOVIE,
                previewContent = painterResource(R.drawable.samper_poster_matrix)
            ),
        )
    ) { }
}
