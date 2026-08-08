package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.domain.model.Genre

internal fun GenreDataModel.toDomain() = Genre(
    id = id,
    name = name
)

internal fun List<GenreDataModel>.toDomain() = map { it.toDomain() }
