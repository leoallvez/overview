package br.dev.singular.overview.data.util.mappers.domainToData

import br.dev.singular.overview.data.model.PredictionFavoriteDataModel
import br.dev.singular.overview.data.model.PredictionInputDataModel
import br.dev.singular.overview.domain.model.PredictionInput

internal fun PredictionInput.toData() = PredictionInputDataModel(
    id = id,
    type = type.toData(),
    title = title,
    genres = genres,
    favorites = favorites.map {
        PredictionFavoriteDataModel(
            title = it.originalTitle.ifEmpty { it.title },
            type = it.type.toData()
        )
    },
    favoriteGenres = favoriteGenres
)
