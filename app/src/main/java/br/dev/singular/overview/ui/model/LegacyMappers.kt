package br.dev.singular.overview.ui.model

import br.dev.singular.overview.data.model.media.GenreLegacy
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import java.util.Date

//TODO: this file going to be delete in the future

fun Media.toUi() = MediaUiModel(
    id = apiId,
    title = getLetter(),
    previewDrawableRes = null,
    posterURL = getPosterImage(),
    type = MediaUiType.getByName(getType())
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
