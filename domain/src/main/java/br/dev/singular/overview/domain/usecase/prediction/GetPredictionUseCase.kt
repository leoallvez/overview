package br.dev.singular.overview.domain.usecase.prediction

import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface IGetPredictionUseCase {
    suspend operator fun invoke(input: PredictionInput): UseCaseState<Prediction>
}

class GetPredictionUseCase(
    private val getter: GetByParam<Prediction, PredictionInput>
) : IGetPredictionUseCase {

    override suspend fun invoke(input: PredictionInput): UseCaseState<Prediction> {
        return when (val state = runSafely { getter.getByParam(input) }) {
            is UseCaseState.Success -> UseCaseState.Success(state.data)
            is UseCaseState.Failure -> state
        }
    }
}
