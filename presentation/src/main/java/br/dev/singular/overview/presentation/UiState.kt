package br.dev.singular.overview.presentation

/**
 * Represents the state of the UI, which can be [Loading], [Success], or [Error].
 *
 * @param T The type of data held by the state.
 */
sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}
