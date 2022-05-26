package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.*
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao
) {

    fun getAll(): List<Suggestion> = dao.getAll()

    suspend fun update(vararg entities: Suggestion) = dao.update(*entities)

    fun getWithAudioVisualItems(): Map<Suggestion, List<AudioVisualItem>> {
        return dao.getWithAudioVisualItems()
    }

    fun hasCache(): Boolean {
        return dao.getWithAudioVisualItems().isNotEmpty()
    }
}
