package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUIModel

@Composable
internal fun getMediaMocks() = listOf(
    MediaUIModel(
        id = 1,
        title = "Matrix",
        posterURLPath = "https://imagens.com/movie.jpg",
        mediaType = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.samper_poster_matrix)
    ),
    MediaUIModel(
        id = 1,
        title = "Matrix",
        posterURLPath = "https://imagens.com/movie.jpg",
        mediaType = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.samper_poster_matrix)
    ),
    MediaUIModel(
        id = 1,
        title = "Matrix",
        posterURLPath = "https://imagens.com/movie.jpg",
        mediaType = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.samper_poster_matrix)
    ),
    MediaUIModel(
        id = 1,
        title = "Matrix",
        posterURLPath = "https://imagens.com/movie.jpg",
        mediaType = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.samper_poster_matrix)
    )
)

