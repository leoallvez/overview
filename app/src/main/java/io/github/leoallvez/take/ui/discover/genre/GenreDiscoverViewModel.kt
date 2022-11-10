package io.github.leoallvez.take.ui.discover.genre

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.DiscoverRepository
import javax.inject.Inject

@HiltViewModel
class GenreDiscoverViewModel @Inject constructor(
    private val _repository: DiscoverRepository
) : ViewModel() {

    fun loadDada(genreId: Long, mediaType: String) =
        _repository.discoverByGenre(genreId, mediaType)
}
