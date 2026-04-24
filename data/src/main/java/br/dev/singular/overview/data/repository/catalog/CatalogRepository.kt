package br.dev.singular.overview.data.repository.catalog

import br.dev.singular.overview.data.local.source.ICatalogLocalDataSource
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import timber.log.Timber
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    private val localSource: ICatalogLocalDataSource,
    private val remoteSource: ICatalogRemoteDataSource
) : GetAll<Catalog>, Delete<Catalog> {

    override suspend fun getAll(): List<Catalog> {
        return localSource.getAll().ifEmpty {
            try {
                remoteSource.getAll().also { localSource.insert(it) }
            } catch (e: Exception) {
                Timber.e(e)
                emptyList()
            }
        }.map { it.toDomain() }
    }

    override suspend fun delete(vararg items: Catalog) {
        localSource.delete(items.map { it.toData() })
    }
}
