package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.Suggestion
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao,
){
    suspend fun save(vararg entities: Suggestion) = dao.insert(*entities)

    fun getByType(type: String): List<Suggestion> = dao.getByType(type)

    suspend fun deleteAll() = dao.deleteAll()

    fun getByTypeWithMovies(type: String) = dao.getByTypeWithMovies(type)

    fun hasMovieCache(type: String): Boolean {
        return dao.getByTypeWithMovies(type).any { it.movies.isNotEmpty() }
    }
}
