package br.dev.singular.overview.data.source.media.local

import br.dev.singular.overview.data.db.dao.MediaDao
import br.dev.singular.overview.data.model.media.MediaEntity
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaDao
) {
    fun insert(models: List<MediaEntity>) = _dao.insert(models)
    fun getLiked() = _dao.getLiked()
    fun deleteNotLiked() = _dao.deleteNotLiked()
    fun getIndicated() = _dao.getIndicated()
    fun updateLike(model: MediaEntity) = _dao.updateLike(model)
}
