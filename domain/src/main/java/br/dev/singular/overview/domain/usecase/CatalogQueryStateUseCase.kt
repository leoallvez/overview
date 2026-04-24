package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.repository.Update
import kotlinx.coroutines.flow.Flow

interface ICatalogQueryStateUseCase {

    fun observe(): Flow<QueryState?>
    suspend fun get(): QueryState?
    suspend fun save(query: QueryState?)
}

class CatalogQueryStateUseCase(
    private val getter: Get<QueryState?>,
    private val updater: Update<QueryState?>,
    private val observer: Observe<QueryState?>
) : ICatalogQueryStateUseCase {

    override fun observe(): Flow<QueryState?> = observer.observe()

    override suspend fun get() = getter.get()

    override suspend fun save(query: QueryState?) = updater.update(query)
}
