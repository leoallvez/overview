package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.UiState

fun <T> UseCaseState<T>.toUiState() = when (this) {
    is UseCaseState.Failure -> UiState.Error()
    is UseCaseState.Success -> UiState.Success(data)
}

fun <T, R> UseCaseState<T>.toUiState(transform: (T) -> R): UiState<R> {
    return when (this) {
        is UseCaseState.Success -> UiState.Success(transform(data))
        is UseCaseState.Failure -> UiState.Error()
    }
}
