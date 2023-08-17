package br.com.deepbyte.overview.data.source.media.local.suggestion

import br.com.deepbyte.overview.data.db.dao.MediaDao
import br.com.deepbyte.overview.data.model.media.MediaEntity
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaDao
) {
    fun insert(vararg medias: MediaEntity) = _dao.insert(*medias)

    fun getAll() = _dao.getAll()

    fun deleteAll() = _dao.deleteAll()
}
