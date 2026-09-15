package br.dev.singular.overview.domain.usecase

import java.util.Calendar
import java.util.Date
import kotlin.coroutines.cancellation.CancellationException

sealed class UseCaseState<out T> {
    data class Success<out T>(val data: T) : UseCaseState<T>()
    data class Failure(val type: FailType) : UseCaseState<Nothing>()
}

sealed class FailType {
    object Invalid : FailType()
    object NothingFound : FailType()
    object Unauthorized : FailType()
    data class Exception(val throwable: Throwable) : FailType()
}

suspend fun <T> runSafely(action: suspend () -> T): UseCaseState<T> {
    return runCatching {
        action()
    }.fold(
        onSuccess = { UseCaseState.Success(it) },
        onFailure = {
            if (it is CancellationException) throw it
            UseCaseState.Failure(FailType.Exception(it))
        }
    )
}

fun Date.adjustDate(days: Int): Date {
    return Calendar.getInstance().apply {
        time = this@adjustDate
        add(Calendar.DAY_OF_MONTH, days)
    }.time
}
