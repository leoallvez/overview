package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.domain.model.Genre

fun Genre.toData() = GenreDataModel(
    id = id,
    name = name
)
