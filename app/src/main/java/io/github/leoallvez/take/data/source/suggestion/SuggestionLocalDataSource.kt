package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.*
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionsDao
) {
    suspend fun update(vararg entities: Suggestion) = dao.update(*entities)

    fun getByType(type: String): List<Suggestion> = dao.getByType(type)

    fun getWithMovies(): Map<Suggestion, List<AudioVisualItem>> {
        return dao.getWithAudioVisualItem()
    }

    fun hasMoviesCache(): Boolean {
        return dao.getWithAudioVisualItem()
            .any { (_, items) ->
                items.isNotEmpty()
            }
    }

    fun getWithTvShows(): Map<Suggestion, List<AudioVisualItem>> {
        return dao.getWithAudioVisualItem()
    }

    fun hasTvShowsCache(): Boolean {
        return dao.getWithAudioVisualItem()
            .any { (_, items) ->
                items.isNotEmpty()
            }
    }
}
