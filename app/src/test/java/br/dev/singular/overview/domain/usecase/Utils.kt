package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.model.Suggestion
import java.util.Date

fun createStreamingMock(lastUpdate: Date = Date()): Streaming {
    return Streaming(
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

fun createMediaMock(isLiked: Boolean = false, lastUpdate: Date = Date()): Media {
    return Media(
        id = 1L,
        type = MediaType.MOVIE,
        title = "A",
        posterPath = "path/to/poster",
        isLiked = isLiked,
        lastUpdate = lastUpdate
    )
}

fun createPersonMock(): Person {
    return Person(
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
