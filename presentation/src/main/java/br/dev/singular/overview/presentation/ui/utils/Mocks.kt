package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaType
import br.dev.singular.overview.presentation.model.MediaUIModel

@Composable
internal fun getMediaMocks(): List<MediaUIModel> {

    val model = MediaUIModel(
        id = 1,
        title = "The Equinox",
        posterURLPath = "https://imagens.com/movie.jpg",
        type = MediaType.MOVIE,
        previewContent = painterResource(R.drawable.sample_poster)
    )

    return listOf(
        model.copy(
            id = 1,
            title = "The Equinox 1"
        ),
        model.copy(
            id = 2,
            title = "The Equinox: Warriors Against the Infinite Swarm from the Dark Expanse"
        ),
        model.copy(
            id = 3,
            title = "The Equinox 3"
        ),
        model.copy(
            id = 4,
            title = "The Equinox 4"
        ),
        model.copy(
            id = 5,
            title = "The Equinox 5"
        ),
        model.copy(
            id = 6,
            title = "The Equinox 6"
        ),
        model.copy(
            id = 7,
            title = "The Equinox 7"
        ),
        model.copy(
            id = 8,
            title = "The Equinox 8"
        ),
    )
}

