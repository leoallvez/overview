package br.com.deepbyte.overview.ui.streaming

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.data.repository.media.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StreamingExploreViewModel @Inject constructor(
    private val _repository: MediaRepository
) : ViewModel() {

    fun getPaging(streamingApiId: Long) = _repository.getPaging(listOf(streamingApiId))
}
