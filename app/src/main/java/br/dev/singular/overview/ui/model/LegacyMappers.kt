package br.dev.singular.overview.ui.model

import androidx.paging.PagingData
import androidx.paging.map
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaDataType.MOVIE
import br.dev.singular.overview.data.model.MediaDataType.TV
import br.dev.singular.overview.data.model.media.GenreLegacy
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.utils.mappers.buildPosterURL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

//TODO: this file going to be delete in the future

fun Media.toUi() = MediaUiModel(
    id = apiId,
    title = getLetter(),
    previewDrawableRes = null,
    posterURL = getPosterImage(),
    type = MediaUiType.getByName(getType())
)

fun MediaDataModel.toUi() = MediaUiModel(
    id = id,
    title = betterTitle,
    previewDrawableRes = null,
    posterURL = buildPosterURL(posterPath),
    type = type.toUi()
)

fun StreamingEntity.toDomain() = Catalog(
    id = apiId,
    name = name,
    logoPath = logoPath,
    priority = priority,
    display = true,
    lastUpdate = Date()
)

fun GenreLegacy.toUi() = GenreUiModel(
    id = id,
    name = name
)

private fun MediaDataType.toUi() = when (this) {
    MOVIE -> MediaUiType.MOVIE
    TV -> MediaUiType.TV
    else -> MediaUiType.ALL
}

fun Flow<PagingData<MediaDataModel>>.toUi() = map { it.map(MediaDataModel::toUi) }
