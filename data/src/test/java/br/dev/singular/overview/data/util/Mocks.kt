package br.dev.singular.overview.data.util

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.SuggestionDataModel

val suggestionModels = listOf(
    SuggestionDataModel(
        id = 1,
        order = 1,
        type = MediaDataType.TV,
        sourceKey = "suggestion_the_witcher",
        isActive = true
    ),
    SuggestionDataModel(
        id = 2,
        order = 2,
        type = MediaDataType.MOVIE,
        sourceKey = "suggestion_dark",
        isActive = true
    )
)

val mediaDataModel = MediaDataModel(
    id = 1,
    name = "Test Name",
    title = "Test Title",
    posterPath = "/test.jpg",
    type = MediaDataType.MOVIE,
    isLiked = false
)
