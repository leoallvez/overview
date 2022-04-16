package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.Suggestion
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao,
){
    suspend fun save(vararg entities: Suggestion) = dao.insert(*entities)

    suspend fun getAll() = dao.getAll()

    suspend fun deleteAll() = dao.deleteAll()
}
