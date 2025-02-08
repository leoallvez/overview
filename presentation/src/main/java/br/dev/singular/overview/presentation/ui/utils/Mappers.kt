package br.dev.singular.overview.presentation.ui.utils

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.UIState
import br.dev.singular.overview.presentation.model.MediaUIType

fun MediaType.toMediaUIType() = when(this) {
    MediaType.MOVIE -> MediaUIType.MOVIE
    MediaType.TV -> MediaUIType.TV
    MediaType.ALL -> MediaUIType.ALL
    else -> MediaUIType.UNKNOWN
}

fun <T> UseCaseState<T>.toUiState() = when (this) {
    is UseCaseState.Failure -> UIState.Error()
    is UseCaseState.Success -> UIState.Success(data)
}

fun <T, R> UseCaseState<T>.toUiState(transform: (T) -> R): UIState<R> {
    return when (this) {
        is UseCaseState.Success -> UIState.Success(transform(data))
        is UseCaseState.Failure -> UIState.Error()
    }
}
