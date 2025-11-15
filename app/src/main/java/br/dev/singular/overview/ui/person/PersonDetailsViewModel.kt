package br.dev.singular.overview.ui.person

import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.data.repository.person.IPersonRepository
import br.dev.singular.overview.di.DisplayAds
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.ui.AdViewModel
import br.dev.singular.overview.ui.PersonUiState
import br.dev.singular.overview.ui.UiState
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    @DisplayAds adsManager: RemoteConfig<Boolean>,
    private val _repository: IPersonRepository,
    @param:MainDispatcher private val _dispatcher: CoroutineDispatcher
) : AdViewModel(adsManager) {

    private val _uiState = MutableStateFlow<PersonUiState>(UiState.Loading())
    val uiState: StateFlow<PersonUiState> = _uiState

    fun load(apiId: Long) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(_dispatcher) {
            _repository.getItem(apiId).collect { result ->
                _uiState.value = result.toUiState()
            }
        }
    }
}
