package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.db.dao.StreamingDao
import br.com.deepbyte.overview.data.model.provider.Streaming
import javax.inject.Inject

class StreamingLocalDataSource @Inject constructor(
    private val _dao: StreamingDao
) {
    fun insert(vararg streaming: Streaming) = _dao.insert(*streaming)
    fun update(vararg streaming: Streaming) = _dao.update(*streaming)

    fun isEmpty(): Boolean = _dao.getAll().isEmpty()

    fun getAll(): List<Streaming> = _dao.getAll()
}
