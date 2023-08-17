package br.com.deepbyte.overview.data.source.media.local

import br.com.deepbyte.overview.data.db.dao.MediaTypeDao
import br.com.deepbyte.overview.data.model.media.MediaTypeEntity
import javax.inject.Inject

class MediaTypeLocalDataSource @Inject constructor(
    private val _dao: MediaTypeDao
) {
    fun getAll() = _dao.getAll()
    fun isEmpty() = _dao.getAll().isEmpty()
    fun insert(mediaType: List<MediaTypeEntity>) = _dao.insert(mediaType)
}
