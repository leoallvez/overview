package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.model.provider.StreamingEntity
import javax.inject.Inject

class StreamingLocalDataSource @Inject constructor(
    private val _dao: StreamingDao
) {
    fun insert(vararg models: StreamingEntity) = _dao.insert(*models)

    fun getItems(): List<StreamingEntity> = _dao.getAll()

    fun getAllSelected(): List<StreamingEntity> = _dao.getAllSelected()

    fun upgrade(streamings: List<StreamingEntity>) = _dao.upgrade(streamings)
}
