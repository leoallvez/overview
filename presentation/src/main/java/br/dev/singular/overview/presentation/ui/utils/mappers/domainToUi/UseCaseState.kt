package br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi

import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.UiState

@PublishedApi
internal inline fun <T, R> UseCaseState<T>.uiStateTransform(transform: (T) -> R): UiState<R> =
    when (this) {
        is UseCaseState.Success -> UiState.Success(transform(data))
        is UseCaseState.Failure -> UiState.Error()
    }

inline fun <T, R> UseCaseState<T>.toUiState(transform: (T) -> R): UiState<R> =
    uiStateTransform(transform)

inline fun <T : Any, R : Any> UseCaseState<T?>.toUiStateNullable(
    crossinline transform: (T) -> R
): UiState<R?> = uiStateTransform { it?.let(transform) }
