package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionDao
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.MediaItem
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val _dao: SuggestionDao
) {

    fun getAll(): List<Suggestion> = _dao.getAll()

    suspend fun update(vararg entities: Suggestion) = _dao.update(*entities)

    fun getWithMediaItems(): Map<Suggestion, List<MediaItem>> {
        return _dao.getAllWithMediaItems()
    }
}
