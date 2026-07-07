package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.MovieDetailsDataModel
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MovieDetails

internal fun MovieDetailsDataModel.toDomain() = MovieDetails(
    id = id,
    title = betterTitle,
    releaseDate = releaseDate,
    runtime = runtime,
    posterPath = posterPath,
    backdropPath = backdropPath,
    overview = overview,
    genres = genres.toDomain(),
    credits = credits.toDomain(),
    directors = credits.crew
        .filter { it.job.equals("Director", ignoreCase = true) }
        .map { it.name },
    videos = videos.toDomain(),
    catalogs = catalogs.toDomain(),
    similar = similar.results.map { it.toDomain(typeOverride = MediaType.MOVIE) },
)
