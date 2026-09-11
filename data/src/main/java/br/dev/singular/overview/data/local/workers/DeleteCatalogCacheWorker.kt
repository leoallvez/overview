package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.di.DeleteCatalog
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeleteCatalogCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @param:DeleteCatalog
    private val useCase: IDeleteUseCase
) : UseCaseWorker(context, params) {

    override suspend fun runWork() = useCase()
}
