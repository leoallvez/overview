package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.*
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao,
){
    suspend fun save(vararg entities: Suggestion) = dao.insert(*entities)

    fun getByType(type: String): List<Suggestion> = dao.getByType(type)

    suspend fun deleteAll() = dao.deleteAll()

    fun getWithMovies(): Map<Suggestion, List<Movie>> {
        return dao.getWithMovies()
    }

    fun hasMoviesCache(): Boolean {
        return dao.getWithMovies()
            .any { (_, movies) ->
                movies.isNotEmpty()
            }
    }

    fun getWithTvShows(): Map<Suggestion, List<TvShow>> {
        return dao.getWithTvShows()
    }

    fun hasTvShowsCache(): Boolean {
        return dao.getWithTvShows()
            .any { (_, tvShows) ->
                tvShows.isNotEmpty()
            }
    }
}
