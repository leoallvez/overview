package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.di.domain.DeleteMedias
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeleteMediasCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @param:DeleteMedias
    private val useCase: IDeleteUseCase
) : UseCaseWorker(context, params) {

    override suspend fun runWork() = useCase()
}

