package br.dev.singular.overview.presentation

/**
 * Represents the state of the UI, which can be [Loading], [Success], or [Error].
 *
 * @param T The type of data held by the state.
 */
sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    data class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}

/**
 * Transforms the data of a [UiState.Success], keeping any other state unchanged.
 *
 * @param transform The function applied to the current data.
 * @return A new [UiState.Success] with the transformed data, or this state if it is not a success.
 */
inline fun <T> UiState<T>.mapSuccess(transform: (T) -> T): UiState<T> =
    if (this is UiState.Success) copy(data = transform(data)) else this

/**

 * Represents the current state of the UI, which can be [Loading], [Success],
 * or [Error].
 */
sealed interface ActionState {

    data object Idle : ActionState

    data object Loading : ActionState

    data object Success : ActionState

    data object Error : ActionState
}
