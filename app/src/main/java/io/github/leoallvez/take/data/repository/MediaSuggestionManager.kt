package io.github.leoallvez.take.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.MediaSuggestion
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MediaSuggestionManager @Inject constructor(
    private val _suggestionRepository: SuggestionRepository,
    private val _mediaRepository: MediaRepository,
) {

    private val _featured = MutableLiveData<List<MediaItem>>()
    val featured: LiveData<List<MediaItem>> = _featured

    private val _mediaSuggestions = MutableLiveData<List<MediaSuggestion>>()
    val mediaSuggestions: LiveData<List<MediaSuggestion>> = _mediaSuggestions

    suspend fun refresh() {
        _suggestionRepository.refresh()
        setAttributes()
    }

    private suspend fun setAttributes() {
        val mediaSuggestions = getMediaSuggestions()
        val result = if(mediaSuggestions.isNotEmpty()) {
            _featured.value = sliceFeatured(mediaSuggestions)
            mediaSuggestions
        } else {
            listOf()
        }
        _mediaSuggestions.value = result
    }

    private suspend fun getMediaSuggestions(): MutableList<MediaSuggestion> {
        return _mediaRepository
            .getData()
            .first()
            .toMutableList()
    }

    private fun sliceFeatured(
        mediaSuggestions: MutableList<MediaSuggestion>
    ): List<MediaItem> {
        val featured = mediaSuggestions.first()
        mediaSuggestions.remove(featured)
        return featured.items.take(MAXIMUM_OF_FEATURED)
    }

    companion object {
        private const val MAXIMUM_OF_FEATURED = 10
    }
}