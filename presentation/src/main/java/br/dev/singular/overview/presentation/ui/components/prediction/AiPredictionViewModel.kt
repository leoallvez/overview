package br.dev.singular.overview.presentation.ui.components.prediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.prediction.IGetPredictionUseCase
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.model.PredictionUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface IAiPredictionViewModel {
    val uiState: StateFlow<PredictionUiModel>
    fun onPredict(model: MediaDetailsUiModel)
}

@HiltViewModel
class AiPredictionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val predictionUseCase: IGetPredictionUseCase,
    private val sessionUseCase: IUserSessionUseCase
) : ViewModel(), IAiPredictionViewModel {

    private val _uiState = MutableStateFlow<PredictionUiModel>(PredictionUiModel.Loading)
    override val uiState: StateFlow<PredictionUiModel> = _uiState.asStateFlow()

    init {
        observeSession()
    }

    override fun onPredict(model: MediaDetailsUiModel) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = PredictionUiModel.Loading
            _uiState.value = predict(model)
        }
    }

    private fun observeSession() {
        viewModelScope.launch(dispatcher) {
            sessionUseCase.observe()
                .map { user -> user != null }
                .distinctUntilChanged()
                .catch {
                    Timber.e(it)
                    emit(false)
                }
                .collect { isSignedIn ->
                    _uiState.value = if (isSignedIn) {
                        PredictionUiModel.Idle
                    } else {
                        PredictionUiModel.LoginRequired
                    }
                }
        }
    }

    private suspend fun predict(model: MediaDetailsUiModel): PredictionUiModel {
        return when (val result = predictionUseCase(input = model.toDomain())) {
            is UseCaseState.Success -> result.data.toUi()
            is UseCaseState.Failure -> PredictionUiModel.Error
        }
    }
}
