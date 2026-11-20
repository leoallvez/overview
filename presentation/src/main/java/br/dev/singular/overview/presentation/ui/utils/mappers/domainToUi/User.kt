package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.presentation.model.UserUiModel

internal fun User.toUi() = UserUiModel(
    id = id,
    name = name,
    email = email,
    photoURL = photoUrl,
)
