package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.model.provider.Streaming
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StreamingLocalDataSource @Inject constructor(
    private val _dao: StreamingDao
) {
    fun insert(vararg streaming: Streaming) = _dao.insert(*streaming)

    fun update(vararg streaming: Streaming) = _dao.update(*streaming)

    fun getItems(): List<Streaming> = _dao.getAll()

    fun getAllSelected(): Flow<List<Streaming>> = _dao.getAllSelected()

    fun deleteAll() = _dao.deleteAll()
}
