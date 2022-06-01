package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionDao
import io.github.leoallvez.take.data.model.*
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionDao
) {

    fun getAll(): List<Suggestion> = dao.getAll()

    suspend fun update(vararg entities: Suggestion) = dao.update(*entities)

    fun getWithMediaItems(): Map<Suggestion, List<MediaItem>> {
        return dao.getWithMediaItems()
    }
}
