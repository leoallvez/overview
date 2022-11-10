package io.github.leoallvez.take.ui.discover.provider

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.DiscoverRepository
import javax.inject.Inject

@HiltViewModel
class ProviderDiscoverViewModel @Inject constructor(
    private val _repository: DiscoverRepository
) : ViewModel() {

    fun loadDada(providerId: Long, mediaType: String) =
        _repository.discoverOnTvByProvider(providerId, mediaType)
}
