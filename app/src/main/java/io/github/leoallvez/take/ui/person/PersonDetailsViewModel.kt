package io.github.leoallvez.take.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.data.repository.PersonRepository
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias PersonUiState = UiState<PersonResponse?>

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    @AbDisplayAds private val _experiment: AbTest<Boolean>,
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
