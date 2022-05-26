package io.github.leoallvez.take.data.source.movie

import io.github.leoallvez.take.data.db.dao.AudioVisualItemDao
import io.github.leoallvez.take.data.model.AudioVisualItem
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    private val dao: AudioVisualItemDao
) {
    suspend fun update(vararg entities: AudioVisualItem) = dao.update(*entities)
}
