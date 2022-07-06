package io.github.leoallvez.take.ui

sealed class UiState {
    object Error : UiState()
    object Loading : UiState()
    class Success<T>(val data: T) : UiState()
}
