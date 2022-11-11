package br.com.deepbyte.take.ui.discover.genre

import androidx.lifecycle.ViewModel
import br.com.deepbyte.take.data.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenreDiscoverViewModel @Inject constructor(
    private val _repository: DiscoverRepository
) : ViewModel() {

    fun loadDada(genreId: Long, mediaType: String) =
        _repository.discoverByGenre(genreId, mediaType)
}
