package io.github.leoallvez.take.data.repository

import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.SuggestionResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ContentsManager @Inject constructor(
    private val _suggestionRepository: SuggestionRepository,
    private val _mediaRepository: MediaRepository,
) {

    val featured = MutableLiveData<List<MediaItem>>()
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
        return _mediaRepository
            .getData()
            .first()
            .toMutableList()
    }

    private fun sliceFeatured(
        suggestions: MutableList<SuggestionResult>
    ): List<MediaItem> {
        val featured = suggestions.first()
        suggestions.remove(featured)
        return featured.items.take(MAXIMUM_OF_FEATURED)
    }

    companion object {
        private const val MAXIMUM_OF_FEATURED = 10
    }
}