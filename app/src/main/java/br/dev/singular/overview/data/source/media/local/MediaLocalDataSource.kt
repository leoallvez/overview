package br.dev.singular.overview.data.source.media.local

import br.dev.singular.overview.data.db.dao.MediaDao
import br.dev.singular.overview.data.model.media.MediaEntity
import java.util.Date
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaDao
) {
    fun insert(models: List<MediaEntity>) = _dao.insert(models)
    fun getLiked() = _dao.getLiked()
    fun update(model: MediaEntity) = _dao.update(model)
    fun isLiked(apiId: Long) = _dao.isLiked(apiId)
    fun deleteUnlikedOlderThan(date: Date) = _dao.deleteUnlikedOlderThan(date)
}
