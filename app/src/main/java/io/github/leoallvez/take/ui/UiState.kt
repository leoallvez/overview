package io.github.leoallvez.take.ui

sealed class UiState <T> {
    class Error<T> : UiState<T>()
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
}
