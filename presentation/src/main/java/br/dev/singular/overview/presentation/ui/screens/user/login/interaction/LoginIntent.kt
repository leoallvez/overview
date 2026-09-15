package br.dev.singular.overview.presentation.ui.screens.user.login.interaction

sealed class LoginIntent {
    data object Login : LoginIntent()
    data class Authenticate(val idToken: String) : LoginIntent()
    data object Fail : LoginIntent()
    data object Reset : LoginIntent()
    data object Logout : LoginIntent()
}
