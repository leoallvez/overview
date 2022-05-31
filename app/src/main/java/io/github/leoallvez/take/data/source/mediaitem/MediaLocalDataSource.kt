package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.db.dao.MediaItemDao
import io.github.leoallvez.take.data.model.MediaItem
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val dao: MediaItemDao
) {
    suspend fun update(vararg entities: MediaItem) = dao.update(*entities)
}
