package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.domain.model.Person

internal fun PersonDataModel.toDomain() = Person(
    id = id,
    name = name,
    order = order,
    job = job,
    character = formattedCharacterName,
    profilePath = profilePath
)

internal fun List<PersonDataModel>.toDomain() = map { it.toDomain() }
