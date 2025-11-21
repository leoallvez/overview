package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.ui.utils.DateHelper
import br.dev.singular.overview.presentation.ui.utils.mappers.buildImageFullURL
import kotlinx.collections.immutable.toImmutableList

internal fun Person.toUi() = PersonUiModel(
    id = id,
    age = DateHelper(birthday).periodBetween(deathDay),
    job = job,
    name = name,
    birthday = DateHelper(birthday).formattedDate(),
    deathDay = DateHelper(deathDay).formattedDate(),
    biography = biography,
    character = character.substringBefore('/'),
    profileURL = buildImageFullURL(profilePath),
    placeOfBirth = placeOfBirth,
    tvShows = tvShows.map { it.toUi() }.toImmutableList(),
    movies = movies.map { it.toUi() }.toImmutableList()
)
