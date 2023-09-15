package dev.com.singular.overview.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.com.singular.overview.IAnalyticsTracker
import dev.com.singular.overview.data.repository.person.IPersonRepository
import dev.com.singular.overview.di.ShowAds
import dev.com.singular.overview.ui.PersonUiState
import dev.com.singular.overview.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.com.singular.overview.util.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IPersonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PersonUiState>(UiState.Loading())
    val uiState: StateFlow<PersonUiState> = _uiState

    private val _dataNotLoaded
        get() = (_uiState.value is UiState.Success).not()

    fun loadPersonDetails(apiId: Long) = viewModelScope.launch {
        if (_dataNotLoaded) {
            _repository.getItem(apiId).collect { result ->
                _uiState.value = result.toUiState()
            }
        }
    }

    fun refresh(apiId: Long) {
        _uiState.value = UiState.Loading()
        loadPersonDetails(apiId)
    }
}
