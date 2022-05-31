package io.github.leoallvez.take.data.repository

import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.SuggestionResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ContentsManager @Inject constructor(
    private val _suggestionRepository: SuggestionRepository,
    private val _audioVisualRepository: AudioVisualItemRepository,
) {

    val featured = MutableLiveData<List<AudioVisualItem>>()
    val suggestions = MutableLiveData<List<SuggestionResult>>()

    suspend fun refresh() {
        _suggestionRepository.refresh()
        setAttributes()
    }

    private suspend fun setAttributes() {
        val suggestionsList = getSuggestions()
        val result = if(suggestionsList.isNotEmpty()) {
            featured.value = sliceFeatured(suggestionsList)
            suggestionsList
        } else {
            listOf()
        }
        suggestions.value = result
    }

    private suspend fun getSuggestions(): MutableList<SuggestionResult> {
        return _audioVisualRepository
            .getData()
            .first()
            .toMutableList()
    }

    private fun sliceFeatured(
        suggestions: MutableList<SuggestionResult>
    ): List<AudioVisualItem> {
        val featured = suggestions.first()
        suggestions.remove(featured)
        return featured.items.take(MAXIMUM_OF_FEATURED)
    }

    companion object {
        private const val MAXIMUM_OF_FEATURED = 10
    }
}