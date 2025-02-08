package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.ISuggestionLocalDataSource
import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.data.model.toDomainModel
import br.dev.singular.overview.data.network.source.ISuggestionRemoteDataSource
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.DeleteAll
import br.dev.singular.overview.domain.repository.GetAll
import timber.log.Timber
import javax.inject.Inject

class SuggestionRepository @Inject constructor(
    private val localSource: ISuggestionLocalDataSource,
    private val remoteSource: ISuggestionRemoteDataSource
) : GetAll<Suggestion>, DeleteAll<Suggestion> {

    override suspend fun getAll(): List<Suggestion> {
        return localSource.getAll().ifEmpty {
            try {
                remoteSource.getAll().also { saveCache(it) }
            } catch (e: Exception) {
                Timber.e(e)
                emptyList()
            }
        }.map { it.toDomainModel() }
    }

    suspend fun saveCache(items: List<SuggestionDataModel>) = localSource.insert(items)

    override suspend fun deleteAll() = localSource.deleteAll()
}
