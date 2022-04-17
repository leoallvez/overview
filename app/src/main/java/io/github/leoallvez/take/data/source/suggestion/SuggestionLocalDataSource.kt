package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.model.Suggestion.Companion.MOVIE_TYPE
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao,
){
    suspend fun save(vararg entities: Suggestion) = dao.insert(*entities)

    fun getByType(type: String): List<Suggestion> = dao.getByType(type)

    suspend fun deleteAll() = dao.deleteAll()

    fun getByTypeWithMovie(): List<SuggestionResult> {
        return dao.getByTypeWithMovie(MOVIE_TYPE)
            .sortedBy { it.suggestion.order }
            .map { it.toSuggestionResult() }
    }

    fun hasMoviesCache(): Boolean {
        return dao.getByTypeWithMovie(MOVIE_TYPE)
            .any { it.movies.isNotEmpty() }
    }
}
