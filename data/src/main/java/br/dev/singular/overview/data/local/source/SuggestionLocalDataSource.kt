package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.model.SuggestionDataModel
import javax.inject.Inject

interface ISuggestionLocalDataSource {
    suspend fun insert(model: List<SuggestionDataModel>)

    suspend fun getAll(): List<SuggestionDataModel>

    suspend fun deleteAll()
}

class SuggestionLocalDataSource @Inject constructor(
    private val dao: SuggestionDao
) : ISuggestionLocalDataSource {
    override suspend fun insert(model: List<SuggestionDataModel>) =
        dao.insert(*model.toTypedArray())

    override suspend fun getAll(): List<SuggestionDataModel> = dao.getAll()

    override suspend fun deleteAll() = dao.deleteAll()
}
