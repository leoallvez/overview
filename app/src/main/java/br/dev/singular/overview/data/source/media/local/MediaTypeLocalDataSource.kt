package br.dev.singular.overview.data.source.media.local

import br.dev.singular.overview.data.db.dao.MediaTypeDao
import br.dev.singular.overview.data.model.media.MediaTypeEntity
import javax.inject.Inject

class MediaTypeLocalDataSource @Inject constructor(
    private val _dao: MediaTypeDao
) {
    fun getAll() = _dao.getAll()
    fun isEmpty() = _dao.getAll().isEmpty()
    fun insert(model: List<MediaTypeEntity>) = _dao.insert(model)
}
