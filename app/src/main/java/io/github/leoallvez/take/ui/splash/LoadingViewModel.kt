package io.github.leoallvez.take.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.api.repository.loading.LoadingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: LoadingRepository

) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.loadingSuggestion()
        }
    }
}
