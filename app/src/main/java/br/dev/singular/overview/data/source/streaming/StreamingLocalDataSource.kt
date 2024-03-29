package br.dev.singular.overview.data.source.streaming

import br.dev.singular.overview.data.db.dao.StreamingDao
import br.dev.singular.overview.data.model.provider.StreamingEntity
import javax.inject.Inject

class StreamingLocalDataSource @Inject constructor(
    private val _dao: StreamingDao
) {
    fun insert(vararg models: StreamingEntity) = _dao.insert(*models)

    fun getItems(): List<StreamingEntity> = _dao.getAll()

    fun getAllSelected(): List<StreamingEntity> = _dao.getAllSelected()

    fun upgrade(models: List<StreamingEntity>) = _dao.update(models)
}
