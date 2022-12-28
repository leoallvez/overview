package br.com.deepbyte.overview.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// TODO: refactor to new patter
class MediaSuggestionManager @Inject constructor(
    private val _suggestionRepository: SuggestionRepository,
    private val _mediaItemsRepository: MediaItemRepository
) {

    private val _featuredMediaItems = MutableLiveData<List<MediaItem>>()
    val featuredMediaItems: LiveData<List<MediaItem>> = _featuredMediaItems

    private val _mediaSuggestions = MutableLiveData<List<MediaSuggestion>>()
    val mediaSuggestions: LiveData<List<MediaSuggestion>> = _mediaSuggestions

    suspend fun refresh() {
        _suggestionRepository.refresh()
        setAttributes()
    }

    private suspend fun setAttributes() {
        val mediaSuggestions = getMediaSuggestions()
        val result = if (mediaSuggestions.isNotEmpty()) {
            _featuredMediaItems.value = sliceFeatured(mediaSuggestions)
            mediaSuggestions
        } else {
            listOf()
        }
        _mediaSuggestions.value = result
    }

    private suspend fun getMediaSuggestions(): MutableList<MediaSuggestion> {
        return _mediaItemsRepository
            .getData()
            .first()
            .toMutableList()
    }

    private fun sliceFeatured(
        mediaSuggestions: MutableList<MediaSuggestion>
    ): List<MediaItem> {
        val featured = mediaSuggestions.first()
        mediaSuggestions.remove(featured)
        return featured
            .items
            .sortedByDescending { it.suggestionId }
            .take(MAXIMUM_OF_FEATURED)
    }

    companion object {
        private const val MAXIMUM_OF_FEATURED = 5
    }
}