package br.dev.singular.overview.data.local.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.domain.usecase.suggestion.IDeleteSuggestionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeleteSuggestionsCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: IDeleteSuggestionsUseCase
) : UseCaseWorker<Boolean>(context, params) {

    override suspend fun runWork() = useCase()
}
