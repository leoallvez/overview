package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.*
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao
) {
    fun getAll(): List<Suggestion> = dao.getAll()

    suspend fun update(vararg entities: Suggestion) = dao.update(*entities)

    fun getWithAudioVisualItem(): Map<Suggestion, List<AudioVisualItem>> {
        return dao.getWithAudioVisualItem()
    }

    fun hasCache(): Boolean {
        return dao.getWithAudioVisualItem()
            .any { (_, items) ->
                items.isNotEmpty()
            }
    }
}
