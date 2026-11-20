package br.dev.singular.overview.domain.usecase.catalog

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely
import java.util.Date

interface IRefreshCatalogUseCase {
    suspend operator fun invoke(): UseCaseState<Unit>
}

class RefreshCatalogUseCase(
    private val localGetter: GetAll<Catalog>,
    private val remoteGetter: GetAll<Catalog>,
    private val updater: Update<List<Catalog>>,
    private val currentDate: () -> Date = { Date() },
) : IRefreshCatalogUseCase {

    override suspend fun invoke() = runSafely {
        val updated = refreshCached(remotes = getRemoteById())
        updater.update(item = updated)
    }

    private suspend fun getRemoteById() = remoteGetter.getAll().associateBy { it.id }

    private suspend fun refreshCached(remotes: Map<Long, Catalog>): List<Catalog> {
        return localGetter.getAll().mapNotNull { cached ->
            remotes[cached.id]?.let { remote -> cached.refreshWith(remote) }
        }
    }

    private fun Catalog.refreshWith(remote: Catalog) = copy(
        name = remote.name,
        logoPath = remote.logoPath,
        lastUpdate = currentDate(),
    )
}
