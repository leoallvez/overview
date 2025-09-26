package br.dev.singular.overview.presentation

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}
