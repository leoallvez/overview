package br.dev.singular.overview.presentation.ui.screens.streaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.streaming.IGetAllStreamingUseCase
import br.dev.singular.overview.domain.usecase.streaming.IGetSelectedStreamingUseCase
import br.dev.singular.overview.domain.usecase.streaming.ISaveSelectedStreamingUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.SelectStreamingUiModel
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectStreamingViewModel @Inject constructor(
    private val getAllUseCase: IGetAllStreamingUseCase,
    private val getSelectedUseCase: IGetSelectedStreamingUseCase,
    private val saveSelectedUseCase: ISaveSelectedStreamingUseCase
) : ViewModel() {

    private val _refreshEvent = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<SelectStreamingUiModel>> = _refreshEvent
        .flatMapLatest { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading()
        )

    fun onLoad() = _refreshEvent.tryEmit(Unit)

    private fun loadData(): Flow<UiState<SelectStreamingUiModel>> = flow {
        emit(UiState.Loading())
        try {

            val selected = getSelectedUseCase()
            val list = getAllUseCase()

            if (list.isNotEmpty()) {
                emit(UiState.Success(
                    data = SelectStreamingUiModel(
                        selectedId = selected?.id,
                        streaming = list.toUi().toImmutableList()
                    )
                ))
            } else {
                emit(UiState.Error())
            }
        } catch (e: Exception) {
            emit(UiState.Error())
        }
    }

    fun onSelect(streaming: StreamingUiModel) {
        viewModelScope.launch {
            saveSelectedUseCase(streaming.toDomain())
        }
    }
}
