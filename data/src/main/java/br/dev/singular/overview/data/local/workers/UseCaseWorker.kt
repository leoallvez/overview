package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import timber.log.Timber

abstract class UseCaseWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    abstract suspend fun runWork(): UseCaseState<*>

    // Lets a worker report its result to whoever observes the work.
    protected open fun toOutputData(data: Any?): Data = Data.EMPTY

    override suspend fun doWork(): Result {
        return when (val result = runWork()) {
            is UseCaseState.Success -> Result.success(toOutputData(result.data))
            is UseCaseState.Failure -> {
                val exception = (result.type as? FailType.Exception)?.throwable
                Timber.e(exception, "Worker ${this::class.simpleName} failed")
                Result.failure()
            }
        }
    }
}
