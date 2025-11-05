package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.ISuggestionLocalDataSource
import br.dev.singular.overview.data.network.source.ISuggestionRemoteDataSource
import br.dev.singular.overview.data.util.mappers.toData
import br.dev.singular.overview.data.util.mappers.toDomain
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import timber.log.Timber
import javax.inject.Inject

class SuggestionRepository @Inject constructor(
    private val localSource: ISuggestionLocalDataSource,
    private val remoteSource: ISuggestionRemoteDataSource
) : GetAll<Suggestion>, Delete<Suggestion> {

    override suspend fun getAll(): List<Suggestion> {
        return localSource.getAll().ifEmpty {
            try {
                remoteSource.getAll().also { localSource.insert(it) }
            } catch (e: Exception) {
                Timber.e(e)
                emptyList()
            }
        }.map { it.toDomain() }
    }

    override suspend fun delete(vararg items: Suggestion) {
        localSource.delete(items.map { it.toData() })
    }
}
