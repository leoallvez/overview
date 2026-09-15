package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import br.dev.singular.overview.domain.usecase.media.ISyncFavoritesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncFavoritesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: ISyncFavoritesUseCase
) : UseCaseWorker(context, params) {

    override suspend fun runWork() = useCase()

    override fun toOutputData(data: Any?) = workDataOf(HAS_CHANGES to (data == true))

    companion object {
        const val HAS_CHANGES = "has_changes"
    }
}
