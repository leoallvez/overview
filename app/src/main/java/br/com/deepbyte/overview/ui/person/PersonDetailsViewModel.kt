package br.com.deepbyte.overview.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.abtesting.AbTesting
import br.com.deepbyte.overview.data.repository.PersonRepository
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.di.AbDisplayAds
import br.com.deepbyte.overview.ui.PersonUiState
import br.com.deepbyte.overview.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    @AbDisplayAds private val _experiment: AbTesting<Boolean>,
    private val _repository: PersonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PersonUiState>(UiState.Loading())
    val uiState: StateFlow<PersonUiState> = _uiState

    fun loadPersonDetails(apiId: Long) = viewModelScope.launch {
        _repository.getPersonDetails(apiId).collect { result ->
            val isSuccess = result is DataResult.Success
            _uiState.value = if (isSuccess) UiState.Success(result.data) else UiState.Error()
        }
    }

    fun refresh(apiId: Long) {
        _uiState.value = UiState.Loading()
        loadPersonDetails(apiId)
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = _experiment.execute())
    }
}
