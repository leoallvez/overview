package br.com.deepbyte.overview.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.data.repository.SearchRepository
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    private val _repository: SearchRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            _repository.search("A")
        }
    }
}
