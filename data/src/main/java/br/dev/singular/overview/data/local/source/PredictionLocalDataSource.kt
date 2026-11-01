package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.PredictionDao
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PredictionDataModel
import javax.inject.Inject

interface IPredictionLocalDataSource {
    suspend fun insert(model: PredictionDataModel)

    suspend fun get(id: Long, type: MediaDataType): PredictionDataModel?

    suspend fun clear()
}

class PredictionLocalDataSource @Inject constructor(
    private val dao: PredictionDao
) : IPredictionLocalDataSource {

    override suspend fun insert(model: PredictionDataModel) = dao.insert(model)

    override suspend fun get(id: Long, type: MediaDataType) = dao.get(id, type)

    override suspend fun clear() = dao.deleteAll()
}
