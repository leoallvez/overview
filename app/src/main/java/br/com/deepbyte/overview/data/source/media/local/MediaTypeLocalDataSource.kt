package br.com.deepbyte.overview.data.source.media.local

import br.com.deepbyte.overview.data.db.dao.MediaTypeDao
import br.com.deepbyte.overview.data.model.media.MediaType
import javax.inject.Inject

class MediaTypeLocalDataSource @Inject constructor(
    private val _dao: MediaTypeDao
) {

    fun insert(mediaType: List<MediaType>) = _dao.insert(mediaType)

    fun isEmpty(): Boolean = _dao.getAll().isEmpty()

    fun getAll(): List<MediaType> = _dao.getAll()
}
