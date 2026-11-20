package br.dev.singular.overview.data.repository.catalog

import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.GetAll
import javax.inject.Inject

class CatalogRemoteRepository @Inject constructor(
    private val dataSource: ICatalogRemoteDataSource
) : GetAll<Catalog> {

    override suspend fun getAll(): List<Catalog> {
        return dataSource.getAllFromApi().map { it.toDomain() }
    }
}
