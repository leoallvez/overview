package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MediaCredits
import br.dev.singular.overview.data.model.PersonDetailsDataModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.PersonDetails

internal fun PersonDetailsDataModel.toDomain() = PersonDetails(
    id = id,
    job = job,
    name = name,
    birthday = birthday,
    deathDay = deathDay,
    biography = biography,
    character = character,
    profilePath = profilePath,
    placeOfBirth = placeOfBirth,
    tvShows = tvShowsCredits.toMediaDomain(type = MediaType.TV),
    movies = moviesCredits.toMediaDomain(type = MediaType.MOVIE)
)

private fun MediaCredits.toMediaDomain(type: MediaType): List<Media> {
    return list.map { it.toDomain(type) }
}
