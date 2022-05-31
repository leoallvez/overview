package io.github.leoallvez.take.data.repository

import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.MediaSuggestion
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ContentsManager @Inject constructor(
    private val _suggestionRepository: SuggestionRepository,
    private val _mediaRepository: MediaRepository,
) {

    val featured = MutableLiveData<List<MediaItem>>()
    val mediaSuggestions = MutableLiveData<List<MediaSuggestion>>()

    suspend fun refresh() {
        _suggestionRepository.refresh()
        setAttributes()
    }

    private suspend fun setAttributes() {
        val suggestionsList = getMediaSuggestions()
        val result = if(suggestionsList.isNotEmpty()) {
            featured.value = sliceFeatured(suggestionsList)
            suggestionsList
        } else {
            listOf()
        }
        mediaSuggestions.value = result
    }

    private suspend fun getMediaSuggestions(): MutableList<MediaSuggestion> {
        return _mediaRepository
            .getData()
            .first()
            .toMutableList()
    }

    private fun sliceFeatured(
        suggestions: MutableList<MediaSuggestion>
    ): List<MediaItem> {
        val featured = suggestions.first()
        suggestions.remove(featured)
        return featured.items.take(MAXIMUM_OF_FEATURED)
    }

    companion object {
        private const val MAXIMUM_OF_FEATURED = 10
    }
}