package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.data.source.audiovisualitem.AudioVisualItemLocalDataSource
import io.github.leoallvez.take.data.source.audiovisualitem.AudioVisualItemRemoteDataSource
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.util.toSuggestionResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioVisualItemRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val localDataSource: AudioVisualItemLocalDataSource,
    private val remoteDataSource: AudioVisualItemRemoteDataSource,
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
) {

    suspend fun getData(): Flow<List<SuggestionResult>> {
        return withContext(ioDispatcher) {
            val hasCache = hasCache()
            val results = if (hasCache) getLocalData() else getRemoteData()
            flow { emit(results) }
        }
    }

    private fun hasCache(): Boolean {
        return suggestionLocalDataSource
            .hasCache()
    }

    private suspend fun getRemoteData(): List<SuggestionResult> {
        val results = mutableListOf<SuggestionResult>()
        val suggestions = getSuggestions()
        suggestions.forEach { suggestion ->
            val response = doRequest(suggestion.apiPath)
            if(response is AudiovisualResult.ApiSuccess) {
                val audiovisual = response.content
                saveCache(audiovisual, suggestion)
                val suggestionResult = suggestion.toSuggestionResult(audiovisual)
                results.add(suggestionResult)
            }
        }
        return results
    }

    private fun getSuggestions(): List<Suggestion> {
        return suggestionLocalDataSource.getAll()
    }

    private fun getLocalData(): List<SuggestionResult> {
        return suggestionLocalDataSource
            .getWithAudioVisualItem()
            .map { it.toSuggestionResult() }
    }

    private suspend fun doRequest(apiPath: String): AudiovisualResult {
        return remoteDataSource.get(apiPath)
    }

    private suspend fun saveCache(
        audioVisualItems: List<AudioVisualItem>,
        suggestion: Suggestion
    ) {
        audioVisualItems.forEach {
            it.suggestionId = suggestion.dbId
        }
        localDataSource.update(*audioVisualItems.toTypedArray())
    }
}
