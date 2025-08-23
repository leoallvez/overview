package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion

fun createSuggestionMock(): Suggestion {
    return Suggestion(
        order = 1,
        type = MediaType.MOVIE,
        key = "key1",
        isActive = true,
        medias = emptyList()
    )
}

fun createMediaMock(): Media {
    return Media(
        id = 1L,
        type = MediaType.MOVIE,
        title = "A",
        posterPath = "path/to/poster"
    )
}
