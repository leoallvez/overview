package io.github.leoallvez.take.data.repository.movie

import android.util.Log
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.data.model.MovieSuggestion
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.Suggestion.Companion.MOVIE_TYPE
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

    suspend fun getData(): Flow<List<MovieSuggestion>> {
        return withContext(ioDispatcher) {
            val onCacheTime = suggestionLocalDataSource.getByTypeWithMovies(MOVIE_TYPE)
                .any { it.movies.isNotEmpty() }
            Log.i("db_request", "onCacheTime: $onCacheTime")
            var results = if (onCacheTime) getLocalData() else getRemoteData()
            flow { emit(results) }
        }
    }

    private fun getLocalData(): List<MovieSuggestion> {
        val results =  suggestionLocalDataSource.getByTypeWithMovies(MOVIE_TYPE)
        //return suggestionLocalDataSource.getByTypeWithMovies(MOVIE_TYPE)
        Log.i("db_request", "path: ${ results[0].suggestion.apiPath}, movies size: ${ results[0].movies.size}")
        return results
    }

    private suspend fun getRemoteData(): List<MovieSuggestion> {
        val results = mutableListOf<MovieSuggestion>()
        val suggestions = getMovieSuggestion()
        suggestions.forEach { suggestion ->
            val result = remoteDataSource.get(suggestion.apiPath)
            if(result is ApiSuccess) {
                val movies = result.content as List<Movie>
                Log.i("api_request", "path: ${suggestion.apiPath}, movies size: ${movies.size}")
                saveCache(movies, suggestion.suggestionId)
                results.add(MovieSuggestion(suggestion, movies))
            }

        }
        return results
    }

    private suspend fun getMovieSuggestion(): List<Suggestion> {
        return suggestionLocalDataSource.getByType(MOVIE_TYPE)
    }

    private suspend fun saveCache(movies: List<Movie>, suggestionId: Long) {
        movies.forEach { it.suggestionId = suggestionId}
        localDataSource.save(*movies.toTypedArray())
    }
}
