package br.com.deepbyte.overview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.repository.streaming.IStreamingRepository
import br.com.deepbyte.overview.di.MainDispatcher
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewHomeViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IStreamingRepository,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        viewModelScope.launch(_mainDispatcher) {
            _repository.itemsFilteredByCurrentCountry()
        }
    }
}
