package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.model.MediaDataModel
import javax.inject.Inject

interface IMediaLocalDataSource {
    suspend fun insert(models: List<MediaDataModel>)
    suspend fun getAll(): List<MediaDataModel>
}

class MediaLocalDataSource @Inject constructor (
    private val dao: MediaDao
) : IMediaLocalDataSource {
    override suspend fun insert(models: List<MediaDataModel>) = dao.insert(models)
    override suspend fun getAll() = dao.getAll()
}
