package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaType.ALL
import br.dev.singular.overview.domain.model.MediaType.MOVIE
import br.dev.singular.overview.domain.model.MediaType.TV

internal fun MediaType.toData() = when(this) {
    MOVIE -> MediaDataType.MOVIE
    TV -> MediaDataType.TV
    ALL -> MediaDataType.ALL
    else -> MediaDataType.UNKNOWN
}
