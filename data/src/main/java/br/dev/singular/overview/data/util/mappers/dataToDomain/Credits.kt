package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.CreditsDataModel
import br.dev.singular.overview.domain.model.Credits

internal fun CreditsDataModel.toDomain() = Credits(
    cast = cast.toDomain(),
    crew = crew.toDomain()
)
