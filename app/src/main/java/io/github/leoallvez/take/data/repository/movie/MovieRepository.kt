package io.github.leoallvez.take.data.repository.movie

import io.github.leoallvez.take.data.model.Audiovisual
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.Suggestion.Companion.MOVIE_TYPE
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.source.AudiovisualResult.ApiSuccess
import io.github.leoallvez.take.data.source.movie.MovieLocalDataSource
import io.github.leoallvez.take.data.source.movie.MovieRemoteDataSource
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val localDataSource: MovieLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getData(): Flow<List<SuggestionResult>> {
        return withContext(ioDispatcher) {
            val onCacheTime = hasCache()
            val results = if (onCacheTime) getLocalData() else getRemoteData()
            flow { emit(results) }
        }
    }

    private fun hasCache(): Boolean {
        return suggestionLocalDataSource.hasMoviesCache()
    }

    private fun getLocalData(): List<SuggestionResult> {
        return suggestionLocalDataSource
            .getByTypeWithMovie()
    }

    private suspend fun getRemoteData(): List<SuggestionResult> {
        val results = mutableListOf<SuggestionResult>()
        val suggestions = getSuggestion()
        suggestions.forEach { suggestion ->
            val result = remoteDataSource.get(suggestion.apiPath)
            if(result is ApiSuccess) {
                saveCache(audiovisuals = result.content, suggestion.suggestionId)
                results.add(
                    SuggestionResult(suggestion.titleResourceId, result.content)
                )
            }
        }
        return results
    }

    private fun getSuggestion(): List<Suggestion> {
        return suggestionLocalDataSource.getByType(MOVIE_TYPE)
    }

    private suspend fun saveCache(
        audiovisuals: List<Audiovisual>,
        suggestionId: Long
    ) {
        val movies = audiovisuals as List<Movie>
        movies.forEach { it.suggestionId = suggestionId}
        localDataSource.save(*movies.toTypedArray())
    }
}
