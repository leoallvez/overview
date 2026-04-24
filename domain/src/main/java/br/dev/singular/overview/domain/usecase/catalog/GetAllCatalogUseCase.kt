package br.dev.singular.overview.domain.usecase.catalog

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.GetAll

interface IGetAllCatalogUseCase {
    suspend operator fun invoke(): List<Catalog>
}

class GetAllCatalogUseCase(
    private val getter: GetAll<Catalog>
) : IGetAllCatalogUseCase {

    override suspend fun invoke() = runCatching {
        getter.getAll().filter { it.display }
    }.fold(
        onSuccess = { it },
        onFailure = { listOf() }
    )
}
