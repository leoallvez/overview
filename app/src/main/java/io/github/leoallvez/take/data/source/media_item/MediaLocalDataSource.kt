package io.github.leoallvez.take.data.source.media_item

import io.github.leoallvez.take.data.db.dao.MediaItemDao
import io.github.leoallvez.take.data.model.MediaItem
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaItemDao
) {
    suspend fun update(vararg entities: MediaItem) = _dao.update(*entities)
}
