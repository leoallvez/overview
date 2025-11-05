package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.di.domain.DeleteSuggestions
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeleteSuggestionsCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @param:DeleteSuggestions
    private val useCase: IDeleteUseCase
) : UseCaseWorker(context, params) {

    override suspend fun runWork() = useCase()
}
