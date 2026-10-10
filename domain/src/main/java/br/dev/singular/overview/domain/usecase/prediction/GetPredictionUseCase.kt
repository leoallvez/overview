package br.dev.singular.overview.domain.usecase.prediction

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely
import kotlinx.coroutines.flow.first

interface IGetPredictionUseCase {
    suspend operator fun invoke(input: PredictionInput): UseCaseState<Prediction>
}

class GetPredictionUseCase(
    private val getter: GetByParam<Prediction, PredictionInput>,
    private val favorites: Observe<List<Media>>
) : IGetPredictionUseCase {

    override suspend fun invoke(input: PredictionInput): UseCaseState<Prediction> = runSafely {
        val others = favorites.observe().first()
            .filterNot { it.id == input.id && it.type == input.type }
        if (others.isEmpty()) {
            Prediction.Unavailable
        } else {
            getter.getByParam(
                input.copy(
                    favorites = others.take(MAX_FAVORITES),
                    favoriteGenres = others.flatMap { it.genres }.groupingBy { it }.eachCount()
                )
            )
        }
    }

    private companion object {
        const val MAX_FAVORITES = 10
    }
}
