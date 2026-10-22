package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.UserDataModel
import br.dev.singular.overview.domain.model.User

internal fun UserDataModel.toDomain() = User(
    id = id,
    name = name.orEmpty(),
    email = email.orEmpty(),
    photoUrl = photoUrl.orEmpty(),
)
