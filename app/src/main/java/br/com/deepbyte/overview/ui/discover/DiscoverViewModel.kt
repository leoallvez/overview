package br.com.deepbyte.overview.ui.discover

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.repository.discover.IDiscoverRepository
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IDiscoverRepository
) : ViewModel() {

    fun loadDiscoverByGenre(genreId: Long, mediaType: String) =
        _repository.discoverByGenreId(genreId, mediaType)
}
