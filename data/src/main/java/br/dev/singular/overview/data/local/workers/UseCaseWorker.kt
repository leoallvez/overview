package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import timber.log.Timber
import java.io.IOException

abstract class UseCaseWorker<T>(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    abstract suspend fun runWork(): UseCaseState<T>

    override suspend fun doWork(): Result = when (val result = runWork()) {
        is UseCaseState.Success -> Result.success()
        is UseCaseState.Failure -> handleFailure(result.type)
    }

    private fun handleFailure(type: FailType): Result {
        val throwable = (type as? FailType.Exception)?.throwable
        Timber.e(throwable, "Worker ${this::class.simpleName} failed: $type (attempt $runAttemptCount)")
        return if (throwable is IOException && runAttemptCount < MAX_RETRY_ATTEMPTS) {
            Result.retry()
        } else {
            Result.failure()
        }
    }

    private companion object {
        const val MAX_RETRY_ATTEMPTS = 5
    }
}
