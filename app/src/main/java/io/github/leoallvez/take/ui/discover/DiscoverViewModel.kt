package io.github.leoallvez.take.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.repository.DiscoverRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val _repository: DiscoverRepository
) : ViewModel() {

    fun load(providerId: Long): Flow<PagingData<MediaItem>> =
        _repository.load(providerId, viewModelScope)
}
