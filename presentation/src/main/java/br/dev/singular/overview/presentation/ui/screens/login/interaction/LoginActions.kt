package br.dev.singular.overview.presentation.ui.screens.login.interaction

data class LoginActions(
    val tagPath: String,
    val handleIntent: (LoginIntent) -> Unit = {}
) {

    fun onLogin() {
        handleIntent(LoginIntent.Login)
    }

    fun onLogout() {
        handleIntent(LoginIntent.Logout)
    }
}
