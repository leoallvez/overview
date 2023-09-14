package br.com.deepbyte.overview.data.source.media.local.suggestion

import br.com.deepbyte.overview.data.db.dao.MediaDao
import br.com.deepbyte.overview.data.model.media.MediaEntity
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaDao
) {
    fun insert(models: List<MediaEntity>) = _dao.insert(models)
    fun getLiked() = _dao.getLiked()
    fun deleteNotLiked() = _dao.deleteNotLiked()
    fun getIndicated() = _dao.getIndicated()
}
