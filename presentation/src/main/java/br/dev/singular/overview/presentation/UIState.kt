package br.dev.singular.overview.presentation

sealed class UIState<T> {
    class Loading<T> : UIState<T>()
    class Success<T>(val data: T) : UIState<T>()
    class Error<T> : UIState<T>()
}
