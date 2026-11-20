package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.catalog.IRefreshCatalogUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshCatalogCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: IRefreshCatalogUseCase
) : UseCaseWorker<Unit>(context, params) {

    override suspend fun runWork() = useCase()
}
