package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.source.AudioVisualResult
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

    private val suggestionsWithItems: Map<Suggestion, List<AudioVisualItem>> by lazy {
        suggestionLocalDataSource.getWithAudioVisualItems()
    }

    suspend fun getData(): Flow<List<SuggestionResult>> {
        return withContext(ioDispatcher) {
            val results = if(suggestionsWithItems.isNotEmpty()) {
                getLocalData()
            } else {
                getRemoteData()
            }
            flow { emit(results) }
        }
    }

    private suspend fun getRemoteData(): List<SuggestionResult> {
        val result = mutableListOf<SuggestionResult>()
        getSuggestions().forEach { suggestion ->
            val response = doRequest(suggestion.apiPath)
            if(response is AudioVisualResult.ApiSuccess) {
                val items = response.items
                setForeignKeyOnItems(items, suggestion.dbId)
                saveItems(items)
                val suggestionResult = suggestion.toSuggestionResult(items)
                result.add(suggestionResult)
            }
        }
        return result.sortedBy { it.order }
    }

    private fun getSuggestions(): List<Suggestion> {
        return suggestionLocalDataSource.getAll()
    }

    private fun getLocalData(): List<SuggestionResult> {
        return suggestionsWithItems
            .map { it.toSuggestionResult() }
    }

    private suspend fun doRequest(apiPath: String): AudioVisualResult {
        return remoteDataSource.get(apiPath)
    }

    private fun setForeignKeyOnItems(
        items: List<AudioVisualItem>,
        suggestionId: Long
    ) {
        items.forEach {
            it.suggestionId = suggestionId
        }
    }

    private suspend fun saveItems(
        items: List<AudioVisualItem>
    ) {
        localDataSource.update(*items.toTypedArray())
    }
}
