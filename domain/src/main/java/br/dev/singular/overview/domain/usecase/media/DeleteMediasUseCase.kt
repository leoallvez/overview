package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import br.dev.singular.overview.domain.usecase.adjustDate
import br.dev.singular.overview.domain.usecase.runSafely
import java.util.Date

class DeleteMediasUseCase(
    private val getter: GetAll<Media>,
    private val deleter: Delete<Media>,
    private val maxCacheDate: Date = Date().adjustDate(days = -7)
) : IDeleteUseCase {

    override suspend fun invoke() = runSafely {
        val toDelete = getter.getAll()
            .filterNot { it.isLiked }
            .filter { it.lastUpdate < maxCacheDate }

        toDelete.also { deleter.delete(*it.toTypedArray()) }.isNotEmpty()
    }
}
