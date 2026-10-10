package br.dev.singular.overview.data.repository.prediction

import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPredictionRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import br.dev.singular.overview.domain.repository.GetByParam
import javax.inject.Inject

class PredictionRemoteRepository @Inject constructor(
    private val dataSource: IPredictionRemoteDataSource
) : GetByParam<Prediction, PredictionInput> {

    override suspend fun getByParam(param: PredictionInput): Prediction {
        return when (val response = dataSource.getByMedia(input = param.toData())) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> Prediction.Error
        }
    }
}
