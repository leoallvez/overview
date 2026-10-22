package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.TvShowDetailsDataModel
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.TvShowDetails

internal fun TvShowDetailsDataModel.toDomain() = TvShowDetails(
    id = id,
    name = betterName,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    episodeRuntime = episodeRuntime,
    firstAirDate = firstAirDate,
    posterPath = posterPath,
    backdropPath = backdropPath,
    overview = overview,
    genres = genres.toDomain(),
    credits = credits.toDomain(),
    creators = creators.map { it.name },
    videos = videos.toDomain(),
    catalogs = catalogs.toDomain(),
    similar = similar.results.map { it.toDomain(typeOverride = MediaType.TV) },
)
