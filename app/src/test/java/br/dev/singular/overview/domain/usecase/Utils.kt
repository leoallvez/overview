package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.Suggestion

fun createDomainSuggestionMock(): Suggestion {
    return Suggestion(
        path = "sample/path",
        order = 1,
        type = "movie",
        titleKey = "sampleTitleKey",
        isActive = true,
        medias = listOf(
            Media(
                id = 1L,
                type = "movie",
                letter = "A",
                posterPath = "sample/poster/path"
            )
        )
    )
}

