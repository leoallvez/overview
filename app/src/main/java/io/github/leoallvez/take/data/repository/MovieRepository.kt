package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.*
import io.github.leoallvez.take.data.model.Suggestion.Companion.MOVIE_TYPE
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.data.source.movie.MovieLocalDataSource
import io.github.leoallvez.take.data.source.movie.MovieRemoteDataSource
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.util.toSuggestionResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val localDataSource: MovieLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : AudioVisualRepository(ioDispatcher) {

    override fun hasCache(): Boolean {
        return suggestionLocalDataSource
            .hasMoviesCache()
    }

    override fun getLocalData(): List<SuggestionResult> {
        return suggestionLocalDataSource
            .getWithMovies()
            .map { it.toSuggestionResult() }
    }

    override suspend fun doRequest(apiPath: String): AudiovisualResult {
        return remoteDataSource
            .get(apiPath)
    }

    override fun getSuggestions(): List<Suggestion> {
        return suggestionLocalDataSource
            .getByType(MOVIE_TYPE)
    }

    override suspend fun saveCache(
        audioVisuals: List<AudioVisual>,
        suggestionId: Long
    ) {
        val movies = audioVisuals as List<Movie>
        movies.forEach { it.suggestionId = suggestionId }
        localDataSource.save(*movies.toTypedArray())
    }
}
