package io.github.leoallvez.take.ui.mediadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.MediaDetailsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val _repository: MediaDetailsRepository
) : ViewModel() {
    fun getMediaDetails(id: Long, type: String) {
        viewModelScope.launch {
            _repository.getMediaDetailsResult(id, type)
        }
    }
}
