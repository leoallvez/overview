package br.com.deepbyte.overview.ui.discover.genre

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenreDiscoverViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: DiscoverRepository
) : ViewModel() {

    fun loadDada(genreId: Long, mediaType: String) =
        _repository.discoverByGenre(genreId, mediaType)
}
