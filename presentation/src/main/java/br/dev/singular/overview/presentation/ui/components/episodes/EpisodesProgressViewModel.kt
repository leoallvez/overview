package br.dev.singular.overview.presentation.ui.components.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.model.EpisodesProgressUiModel
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

interface IEpisodesProgressViewModel {
    val uiState: StateFlow<EpisodesProgressUiModel>
}

@HiltViewModel
class EpisodesProgressViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val sessionUseCase: IUserSessionUseCase
) : ViewModel(), IEpisodesProgressViewModel {

    private val _uiState = MutableStateFlow<EpisodesProgressUiModel>(
        EpisodesProgressUiModel.Loading
    )
    override val uiState: StateFlow<EpisodesProgressUiModel> = _uiState.asStateFlow()

    init {
        observeSession()
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
                        EpisodesProgressUiModel.NotStarted
                    } else {
                        EpisodesProgressUiModel.SignedOut
                    }
                }
        }
    }
}
