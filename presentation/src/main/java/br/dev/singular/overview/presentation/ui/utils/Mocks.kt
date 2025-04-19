package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUIModel

@Composable
internal fun getMediaMocks() = listOf(
    MediaUIModel(
        id = 1,
        title = "The Equinox",
        posterURLPath = "https://imagens.com/movie.jpg",
        type = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.sample_poster_the_equinox)
    ),
    MediaUIModel(
        id = 2,
        title = "Iron Titans: Warriors Against the Infinite Swarm from the Dark Expanse",
        posterURLPath = "https://imagens.com/movie.jpg",
        type = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.sample_poster_iron_titans)
    ),
    MediaUIModel(
        id = 3,
        title = "The Haunting",
        posterURLPath = "https://imagens.com/movie.jpg",
        type = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.sample_poster_the_haunting)
    ),
    MediaUIModel(
        id = 4,
        title = "Matrix",
        posterURLPath = "https://imagens.com/movie.jpg",
        type = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.samper_poster_matrix)
    )
)

