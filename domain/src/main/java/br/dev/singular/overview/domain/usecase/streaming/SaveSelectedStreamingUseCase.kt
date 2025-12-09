package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface ISaveSelectedStreamingUseCase {
    suspend operator fun invoke(model: Streaming): UseCaseState<Unit>
}

class SaveSelectedStreamingUseCase(
    private val updater: Update<Streaming>
) : ISaveSelectedStreamingUseCase {

    override suspend fun invoke(model: Streaming) = runSafely {
        updater.update(model)
    }
}
