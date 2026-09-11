package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaDataType.ALL
import br.dev.singular.overview.data.model.MediaDataType.MOVIE
import br.dev.singular.overview.data.model.MediaDataType.TV
import br.dev.singular.overview.domain.model.MediaType

internal fun MediaDataType.toDomain() = when (this) {
    MOVIE -> MediaType.MOVIE
    TV -> MediaType.TV
    ALL -> MediaType.ALL
    else -> MediaType.UNKNOWN
}
