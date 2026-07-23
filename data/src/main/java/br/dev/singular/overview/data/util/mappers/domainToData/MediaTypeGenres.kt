package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.MediaTypeGenreDataModel
import br.dev.singular.overview.domain.model.MediaTypeGenres

fun MediaTypeGenres.toData() = genres.map {
    MediaTypeGenreDataModel(
        type = type.toData(),
        genreId = it.id
    )
}
