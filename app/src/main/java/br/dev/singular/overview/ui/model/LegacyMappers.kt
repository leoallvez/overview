package br.dev.singular.overview.ui.model

import androidx.paging.PagingData
import androidx.paging.map
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaDataType.*
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.utils.mappers.buildImageFullURL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//TODO: this file going to be delete in the future

fun Media.toUi() = MediaUiModel(
    id = apiId,
    title = getLetter(),
    posterURL = getPosterImage(),
    type = MediaUiType.getByName(getType())
)

fun MediaDataModel.toUi() = MediaUiModel(
    id = id,
    title = betterTitle,
    posterURL = buildImageFullURL(posterPath),
    type = type.toUi()
)

private fun MediaDataType.toUi() = when(this) {
    MOVIE -> MediaUiType.MOVIE
    TV -> MediaUiType.TV
    else -> MediaUiType.ALL
}

fun Flow<PagingData<MediaDataModel>>.toUi() = map { it.map(MediaDataModel::toUi) }
