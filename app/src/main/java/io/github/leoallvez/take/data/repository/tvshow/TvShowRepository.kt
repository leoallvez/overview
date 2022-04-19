package io.github.leoallvez.take.data.repository.tvshow

import io.github.leoallvez.take.data.model.*
import io.github.leoallvez.take.data.model.Suggestion.Companion.TV_SHOW_TYPE
import io.github.leoallvez.take.data.source.AudiovisualResult.ApiSuccess
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.data.source.tvshow.TvShowLocalDataSource
import io.github.leoallvez.take.data.source.tvshow.TvShowRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val localDataSource: TvShowLocalDataSource,
    private val remoteDataSource: TvShowRemoteDataSource,
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getData(): Flow<List<SuggestionResult>> {
        return withContext(ioDispatcher) {
            val hasCache = hasCache()
            val results = if (hasCache) getLocalData() else getRemoteData()
            flow { emit(results) }
        }
    }

    private fun hasCache(): Boolean {
        return suggestionLocalDataSource.hasTvShowsCache()
    }

    private fun getLocalData(): List<SuggestionResult> {
        return suggestionLocalDataSource
            .getWithTvShows()
    }

    private suspend fun getRemoteData(): List<SuggestionResult> {
        val results = mutableListOf<SuggestionResult>()
        val suggestions = getSuggestions()
        suggestions.forEach { suggestion ->
            val response = remoteDataSource.get(suggestion.apiPath)
            if(response is ApiSuccess) {
                val audiovisual = response.content
                saveCache(audiovisual, suggestion.suggestionId)
                val suggestionResult = suggestion.toSuggestionResult(audiovisual)
                results.add(suggestionResult)
            }
        }
        return results
    }

    private fun getSuggestions(): List<Suggestion> {
        return suggestionLocalDataSource.getByType(TV_SHOW_TYPE)
    }

    private suspend fun saveCache(
        audiovisuals: List<Audiovisual>,
        suggestionId: Long
    ) {
        val tvShows = audiovisuals as List<TvShow>
        tvShows.forEach { it.suggestionId = suggestionId }
        localDataSource.save(* tvShows.toTypedArray())
    }
}
