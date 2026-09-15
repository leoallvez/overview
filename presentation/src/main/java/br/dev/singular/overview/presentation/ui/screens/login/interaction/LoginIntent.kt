package br.dev.singular.overview.presentation.ui.screens.login.interaction

sealed class LoginIntent {
    data object Login : LoginIntent()
    data object Logout : LoginIntent()
}
