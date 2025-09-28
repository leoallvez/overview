package br.dev.singular.overview.ui.model

import androidx.paging.PagingData
import androidx.paging.map
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//TODO: this file going to be delete in the future

fun Media.toUiModel(): MediaUiModel {
    return MediaUiModel(
        id = apiId,
        title = getLetter(),
        posterURL = getPosterImage(),
        type = MediaUiType.getByName(getType())
    )
}

fun MediaEntity.toUiModel(): MediaUiModel {
    return MediaUiModel(
        id = apiId,
        title = letter,
        posterURL = getPosterImage(),
        type = MediaUiType.getByName(type)
    )
}

fun Flow<PagingData<MediaEntity>>.toUiModel() =
    map { pagingData -> pagingData.map { it.toUiModel() } }
