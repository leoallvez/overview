package br.dev.singular.overview.domain.usecase.streaming

import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.runSafely
import java.util.Date

class DeleteStreamingUseCase(
    private val getter: GetAll<Streaming>,
    private val deleter: Delete<Streaming>,
    private val maxCacheDate: Date = Date().adjustDate(days = -1)
) : IDeleteUseCase  {

    override suspend fun invoke() = runSafely {
        val toDelete = getter.getAll()
            .filter { it.lastUpdate < maxCacheDate }
        toDelete.also { deleter.delete(*it.toTypedArray()) }.isNotEmpty()
    }
}
