package br.dev.singular.overview.domain.usecase.suggestion

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.runSafely
import java.util.Date

interface IDeleteSuggestionsUseCase : IDeleteUseCase

class DeleteSuggestionsUseCase(
    private val getter: GetAll<Suggestion>,
    private val deleter: Delete<Suggestion>,
    private val maxCacheDate: Date = Date().adjustDate(days = -1)
) : IDeleteSuggestionsUseCase {

    override suspend fun invoke() = runSafely {
        val toDelete = getter.getAll().filter { it.lastUpdate < maxCacheDate }
        toDelete.also { deleter.delete(*it.toTypedArray()) }.isNotEmpty()
    }
}
