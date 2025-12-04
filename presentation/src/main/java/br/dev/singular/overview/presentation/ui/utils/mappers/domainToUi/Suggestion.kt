package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.Suggestion

fun List<Suggestion>.toUi() = associate { it.key to it.medias.map(Media::toUi) }
