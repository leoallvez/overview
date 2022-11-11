package br.com.deepbyte.take.data.source.media_item

import br.com.deepbyte.take.data.db.dao.MediaItemDao
import br.com.deepbyte.take.data.model.MediaItem
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaItemDao
) {
    suspend fun update(vararg entities: MediaItem) = _dao.update(*entities)
}
