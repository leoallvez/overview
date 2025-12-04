package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.UiState

internal fun <T> UseCaseState<T>.toUi() = when (this) {
    is UseCaseState.Failure -> UiState.Error()
    is UseCaseState.Success -> UiState.Success(data)
}

fun <T, R> UseCaseState<T>.toUi(transform: (T) -> R): UiState<R> {
    return when (this) {
        is UseCaseState.Success -> UiState.Success(transform(data))
        is UseCaseState.Failure -> UiState.Error()
    }
}
