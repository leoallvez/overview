package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao,
){
    suspend fun save(vararg entities: Suggestion) = dao.insert(*entities)

    fun getByType(type: String): List<Suggestion> = dao.getByType(type)

    suspend fun deleteAll() = dao.deleteAll()

    fun getWithMovies(): List<SuggestionResult> {
        return dao.getWithMovies()
            .sortedBy { it.suggestion.order }
            .map { it.toSuggestionResult() }
    }

    fun hasMoviesCache(): Boolean {
        return dao.getWithMovies()
            .any { it.movies.isNotEmpty() }
    }

    fun getWithTvShows(): List<SuggestionResult> {
        return dao.getWithTvShows()
            .sortedBy { it.suggestion.order }
            .map { it.toSuggestionResult() }
    }

    fun hasTvShowsCache(): Boolean {
        return dao.getWithTvShows()
            .any { it.tvShows.isNotEmpty() }
    }
}
