package br.dev.singular.overview.presentation.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.UserUiModel
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toActionState
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val useCase: IUserSessionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<UserUiModel?>>(UiState.Loading())
    val uiState: StateFlow<UiState<UserUiModel?>> = _uiState

    private val _actionState = MutableStateFlow<ActionState>(ActionState.Idle)
    val actionState: StateFlow<ActionState> = _actionState

    init {
        viewModelScope.launch(dispatcher) {
            useCase.observe()
                .catch { e ->
                    Timber.e(e)
                    _uiState.value = UiState.Error()
                }
                .collect { user ->
                    _uiState.value = UiState.Success(user?.toUi())
                }
        }
    }

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> _actionState.value = ActionState.Loading
            is LoginIntent.Authenticate -> onAuthenticate(intent.idToken)
            is LoginIntent.Fail -> _actionState.value = ActionState.Error
            is LoginIntent.Reset -> _actionState.value = ActionState.Idle
            is LoginIntent.Logout -> onLogout()
        }
    }

    private fun onAuthenticate(idToken: String) = launchAction { useCase.signIn(idToken) }

    private fun onLogout() = launchAction { useCase.signOut() }

    private fun launchAction(action: suspend () -> UseCaseState<*>) {
        _actionState.value = ActionState.Loading
        viewModelScope.launch(dispatcher) {
            _actionState.value = action().toActionState()
        }
    }
}
