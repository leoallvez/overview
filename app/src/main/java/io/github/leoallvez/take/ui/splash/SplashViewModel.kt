package io.github.leoallvez.take.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.SuggestionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SuggestionRepository
) : ViewModel() {

    fun loadingData() = viewModelScope.launch {
        repository.refresh()
    }
}
