package br.dev.singular.overview.presentation.ui.screens.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.IGetPersonDetailsByIdUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.PersonDetailsUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUiStateNullable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val useCase: IGetPersonDetailsByIdUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<PersonDetailsUiModel?>>(UiState.Loading())
    val uiState: StateFlow<UiState<PersonDetailsUiModel?>> = _uiState

    fun onLoad(id: Long) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(dispatcher) {
            _uiState.value = useCase.invoke(id).toUiStateNullable { it.toUi() }
        }
    }
}
