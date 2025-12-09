package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.StreamingDao
import br.dev.singular.overview.data.model.StreamingDataModel
import javax.inject.Inject

interface IStreamingLocalDataSource {

    suspend fun insert(models: List<StreamingDataModel>)

    suspend fun getAll(): List<StreamingDataModel>

    suspend fun delete(models: List<StreamingDataModel>)
}

class StreamingLocalDataSource @Inject constructor(
    private val dao: StreamingDao,
) : IStreamingLocalDataSource {

    override suspend fun insert(models: List<StreamingDataModel>) {
        dao.insert(*models.toTypedArray())
    }

    override suspend fun getAll() = dao.getAll()

    override suspend fun delete(models: List<StreamingDataModel>) {
        dao.delete(*models.toTypedArray())
    }
}
