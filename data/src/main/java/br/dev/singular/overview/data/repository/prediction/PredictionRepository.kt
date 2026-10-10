package br.dev.singular.overview.data.repository.prediction

import br.dev.singular.overview.data.local.source.IPredictionLocalDataSource
import br.dev.singular.overview.data.model.PredictionInputDataModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPredictionRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.repository.GetByParam
import javax.inject.Inject

class PredictionRepository @Inject constructor(
    private val localSource: IPredictionLocalDataSource,
    private val remoteSource: IPredictionRemoteDataSource
) : GetByParam<Prediction, PredictionInput> {

    override suspend fun getByParam(param: PredictionInput): Prediction {
        val input = param.toData()
        val cached = localSource.get(input.id, input.type)
        return cached?.toDomain() ?: fetch(input)
    }

    private suspend fun fetch(input: PredictionInputDataModel): Prediction {
        return when (val response = remoteSource.getByMedia(input)) {
            is DataResult.Success -> {
                val model = response.data.copy(id = input.id, type = input.type)
                model.toDomain().also { if (it is Prediction.Available) localSource.insert(model) }
            }
            is DataResult.Error -> Prediction.Error
        }
    }
}
