package br.dev.singular.overview.presentation.ui.screens.user.login.interaction

import androidx.compose.runtime.Immutable
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals

@Immutable
data class LoginActions(
    val tagPath: String = "/login",
    val handleIntent: (LoginIntent) -> Unit = {},
    val onRequestIdToken: () -> Unit = {},
    val onShowSnackbar: (UiSnackbarVisuals) -> Unit = {}
) {

    fun onLogin() {
        TagManager.logClick(customPath = tagPath, detail = "sign-in-with-google")
        handleIntent(LoginIntent.Login)
        onRequestIdToken()
    }

    fun onError(visuals: UiSnackbarVisuals) {
        TagManager.logInteraction(customPath = tagPath, detail = "sign-in-error")
        onShowSnackbar(visuals)
        handleIntent(LoginIntent.Reset)
    }
}
