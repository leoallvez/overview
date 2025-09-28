package br.dev.singular.overview.ui.model

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.map
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.data.source.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//TODO: this file going to be delete in the future

fun Media.toUiModel(): MediaUiModel {
    return MediaUiModel(
        id = apiId,
        title = getLetter(),
        posterURL = getPosterImage(),
        type = getType().toMediaUiType()
    )
}

fun MediaEntity.toUiModel(): MediaUiModel {
    return MediaUiModel(
        id = apiId,
        title = letter,
        posterURL = getPosterImage(),
        type = type.toMediaUiType()
    )
}

fun Flow<PagingData<MediaEntity>>.toUiModel() =
    map { pagingData -> pagingData.map { it.toUiModel() } }

fun String.toMediaUiType() = when (this) {
    MediaType.MOVIE.key -> MediaUiType.MOVIE
    else -> MediaUiType.TV
}

fun MediaUiType.toMediaType() = when (this) {
    MediaUiType.MOVIE -> MediaType.MOVIE.key
    else -> MediaType.TV_SHOW.key
}
