package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.CatalogDao
import br.dev.singular.overview.data.model.CatalogDataModel
import javax.inject.Inject

interface ICatalogLocalDataSource {

    suspend fun insert(models: List<CatalogDataModel>)

    suspend fun getAll(): List<CatalogDataModel>

    suspend fun delete(models: List<CatalogDataModel>)
}

class CatalogLocalDataSource @Inject constructor(
    private val dao: CatalogDao,
) : ICatalogLocalDataSource {

    override suspend fun insert(models: List<CatalogDataModel>) {
        dao.insert(*models.toTypedArray())
    }

    override suspend fun getAll() = dao.getAll()

    override suspend fun delete(models: List<CatalogDataModel>) {
        dao.delete(*models.toTypedArray())
    }
}
