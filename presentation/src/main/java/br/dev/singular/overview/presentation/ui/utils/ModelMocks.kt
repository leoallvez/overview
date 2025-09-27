package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType


private const val BASE_TITLE = "The Equinox"
private const val LONG_TITLE = "The Equinox: Warriors Against the Infinite Swarm from the Dark Expanse"

@Composable
internal fun getMediaMocks(count: Int = 10): List<MediaUiModel> {

    val baseModel = MediaUiModel(
        id = 0,
        title = BASE_TITLE,
        posterURL = "https://imagens.com/movie.jpg",
        type = MediaUiType.MOVIE,
        previewContent = painterResource(R.drawable.sample_poster)
    )

    return buildList {
        repeat(count) { index ->
            val title = if (index % 2 == 1) LONG_TITLE else "$BASE_TITLE ${index + 1}"
            add(
                baseModel.copy(
                    id = index.toLong(),
                    title = title
                )
            )
        }
    }
}


