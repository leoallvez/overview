package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.Audiovisual
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
            .map { (s, movies) ->
                toSuggestionResult(suggestion = s, audiovisuals = movies)
            }
    }

    fun hasMoviesCache(): Boolean {
        return dao.getWithMovies()
            .any { map -> map.value.isNotEmpty() }
    }

    fun getWithTvShows(): List<SuggestionResult> {
        return dao.getWithTvShows()
            .map { (s, tvShows) ->
                toSuggestionResult(suggestion = s, audiovisuals = tvShows)
            }
    }

    private fun toSuggestionResult(
        suggestion: Suggestion,
        audiovisuals: List<Audiovisual>
    ): SuggestionResult {
        return SuggestionResult(
            order = suggestion.order,
            titleResourceId = suggestion.titleResourceId,
            audiovisuals = audiovisuals
        )
    }

    fun hasTvShowsCache(): Boolean {
        return dao.getWithTvShows()
            .any { map -> map.value.isNotEmpty() }
    }
}
