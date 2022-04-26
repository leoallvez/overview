package io.github.leoallvez.take.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.SuggestionRepository
import io.github.leoallvez.take.di.IsOnline
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SuggestionRepository,
    @IsOnline private val isOnline: LiveData<Boolean>
) : ViewModel() {

    fun loadingData() = viewModelScope.launch {
        isOnline.asFlow().collect { isOnline ->
            if(isOnline) {
                repository.refresh()
            }
        }
    }
}
