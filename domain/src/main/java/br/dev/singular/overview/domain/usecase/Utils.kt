package br.dev.singular.overview.domain.usecase

sealed class UseCaseState<out T> {
    data class Success<out T>(val data: T) : UseCaseState<T>()
    data class Failure(val type: FailType) : UseCaseState<Nothing>()
}

sealed class FailType {
    object Invalid : FailType()
    object NothingFound : FailType()
    data class Exception(val throwable: Throwable) : FailType()
}
