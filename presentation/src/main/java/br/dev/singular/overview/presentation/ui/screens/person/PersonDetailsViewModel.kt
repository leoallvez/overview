package br.dev.singular.overview.presentation.ui.screens.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.IGetPersonByIdUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val useCase: IGetPersonByIdUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<PersonUiModel?>>(UiState.Loading())
    val uiState: StateFlow<UiState<PersonUiModel?>> = _uiState

    fun onLoad(id: Long) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(dispatcher) {
            _uiState.value = useCase.invoke(id).toUi { it?.toUi() }
        }
    }
}
