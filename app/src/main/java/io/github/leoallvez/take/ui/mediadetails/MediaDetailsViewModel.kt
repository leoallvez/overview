package io.github.leoallvez.take.ui.mediadetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.MediaDetailsRepository
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val _repository: MediaDetailsRepository
) : ViewModel() {
    fun getMediaDetails(id: Long, type: String) {
        _repository.getMediaDetails(id, type)
    }
}