package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IDeleteMediasCacheUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class DeleteMediasCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: IDeleteMediasCacheUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return when (val result = useCase.invoke()) {
            is UseCaseState.Success -> Result.success()
            is UseCaseState.Failure -> {
                Timber.e(result.type.toString())
                Result.failure()
            }
        }
    }
}
