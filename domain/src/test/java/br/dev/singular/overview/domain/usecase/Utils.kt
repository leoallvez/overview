package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.Credits
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MovieDetails
import br.dev.singular.overview.domain.model.PersonDetails
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.model.TvShowDetails
import java.util.Date

fun createCatalogMock(lastUpdate: Date = Date()): Catalog {
    return Catalog(
        id = 1L,
        name = "Netflix",
        priority = 1,
        logoPath = "path/to/logo",
        display = true,
        lastUpdate = lastUpdate
    )
}

fun createSuggestionMock(lastUpdate: Date = Date()): Suggestion {
    return Suggestion(
        order = 1,
        type = MediaType.MOVIE,
        key = "key1",
        isActive = true,
        medias = emptyList(),
        lastUpdate = lastUpdate
    )
}

fun createMediaMock(
    isLiked: Boolean = false,
    lastUpdate: Date = Date(),
    type: MediaType = MediaType.MOVIE
): Media {
    return Media(
        id = 1L,
        type = type,
        title = "A",
        posterPath = "path/to/poster",
        isLiked = isLiked,
        lastUpdate = lastUpdate
    )
}

fun createPersonDetailsMock(): PersonDetails {
    return PersonDetails(
        id = 1,
        job = "Actor",
        name = "Celeste Beaumont",
        birthday = "01/01/1982",
        deathDay = "01/01/2006",
        profilePath = "https://image.tmdb.org/t/p/original",
        biography = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        character = "Himself",
        placeOfBirth = "Modesto, California, USA",
        tvShows = listOf(),
        movies = listOf()
    )
}

fun createTvShowDetailsMock(): TvShowDetails {
    return TvShowDetails(
        id = 1L,
        name = "TV Show Name",
        numberOfSeasons = 5,
        numberOfEpisodes = 50,
        episodeRuntime = listOf(45),
        firstAirDate = "2020-01-01",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        overview = "Overview description",
        creators = listOf("Creator 1", "Creator 2"),
        genres = listOf(),
        credits = Credits(),
        videos = listOf(),
        catalogs = listOf(),
        similar = listOf()
    )
}

fun createMovieDetailsMock(): MovieDetails {
    return MovieDetails(
        id = 1L,
        title = "Movie Title",
        releaseDate = "2020-01-01",
        runtime = 120,
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        overview = "Overview description",
        directors = listOf("Director 1", "Director 2"),
        genres = listOf(),
        credits = Credits(),
        videos = listOf(),
        catalogs = listOf(),
        similar = listOf()
    )
}
