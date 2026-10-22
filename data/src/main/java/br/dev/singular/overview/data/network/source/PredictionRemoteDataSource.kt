package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.data.model.PredictionInputDataModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

interface IPredictionRemoteDataSource {
    suspend fun getByMedia(input: PredictionInputDataModel): DataResult<PredictionDataModel>
}

class PredictionRemoteDataSource @Inject constructor() : IPredictionRemoteDataSource {

    override suspend fun getByMedia(
        input: PredictionInputDataModel
    ): DataResult<PredictionDataModel> {
        delay(MOCK_DELAY)
        return DataResult.Success(PredictionDataModel(MOCK_AFFINITY_RANGE.random()))
    }

    private companion object {
        val MOCK_DELAY = 2.seconds
        val MOCK_AFFINITY_RANGE = 0..100
    }
}
