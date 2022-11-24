package br.com.deepbyte.overview.ui.discover.provider

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.repository.DiscoverRepository
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProviderDiscoverViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: DiscoverRepository
) : ViewModel() {

    fun loadDada(providerId: Long, mediaType: String) =
        _repository.discoverOnTvByProvider(providerId, mediaType)
}
