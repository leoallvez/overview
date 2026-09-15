package br.dev.singular.overview.presentation.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.ISyncFavoritesUseCase
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.UserUiModel
import br.dev.singular.overview.presentation.ui.screens.common.UiEvent
import br.dev.singular.overview.presentation.ui.screens.common.UiEvents
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
    private val syncFavoritesUseCase: ISyncFavoritesUseCase,
    private val uiEvents: UiEvents,
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

    private fun onAuthenticate(idToken: String) {
        _actionState.value = ActionState.Loading
        viewModelScope.launch(dispatcher) {
            val result = useCase.signIn(idToken)
            // A sync failure is ignored here because the sync runs again on the next app start.
            if (result is UseCaseState.Success) syncFavoritesUseCase()
            _actionState.value = result.toActionState()
            // The favorites are shown as soon as the session starts, before the sign in
            // finishes syncing them, so the list must be loaded again.
            uiEvents.trigger(UiEvent.ReloadFavorites)
        }
    }

    private fun onLogout() {
        _actionState.value = ActionState.Loading
        viewModelScope.launch(dispatcher) {
            _actionState.value = useCase.signOut().toActionState()
        }
    }
}
