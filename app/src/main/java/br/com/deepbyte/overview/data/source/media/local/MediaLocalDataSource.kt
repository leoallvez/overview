package br.com.deepbyte.overview.data.source.media.local

import br.com.deepbyte.overview.data.db.dao.MediaItemDao
import br.com.deepbyte.overview.data.model.MediaItem
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaItemDao
) {
    suspend fun update(vararg entities: MediaItem) = _dao.update(*entities)
}