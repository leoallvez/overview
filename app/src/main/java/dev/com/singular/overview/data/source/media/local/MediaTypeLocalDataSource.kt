package dev.com.singular.overview.data.source.media.local

import dev.com.singular.overview.data.db.dao.MediaTypeDao
import dev.com.singular.overview.data.model.media.MediaTypeEntity
import javax.inject.Inject

class MediaTypeLocalDataSource @Inject constructor(
    private val _dao: MediaTypeDao
) {
    fun getAll() = _dao.getAll()
    fun isEmpty() = _dao.getAll().isEmpty()
    fun insert(model: List<MediaTypeEntity>) = _dao.insert(model)
}
