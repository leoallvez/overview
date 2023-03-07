package br.com.deepbyte.overview.ui.streaming

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StreamingExploreViewModel @Inject constructor(
    private val _repository: IMediaPagingRepository
) : ViewModel() {

    fun getPaging(streamingApiId: Long) = _repository.getPaging(listOf(streamingApiId))
}
