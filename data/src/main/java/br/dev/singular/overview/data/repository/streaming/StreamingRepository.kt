package br.dev.singular.overview.data.repository.streaming

import br.dev.singular.overview.data.local.source.IStreamingLocalDataSource
import br.dev.singular.overview.data.network.source.IStreamingRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Streaming
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import timber.log.Timber
import javax.inject.Inject

class StreamingRepository @Inject constructor(
    private val localSource: IStreamingLocalDataSource,
    private val remoteSource: IStreamingRemoteDataSource
) : GetAll<Streaming>, Delete<Streaming> {

    override suspend fun getAll(): List<Streaming> {
        return localSource.getAll().ifEmpty {
            try {
                remoteSource.getAll().also { localSource.insert(it) }
            } catch (e: Exception) {
                Timber.Forest.e(e)
                emptyList()
            }
        }.map { it.toDomain() }
    }

    override suspend fun delete(vararg items: Streaming) {
        localSource.delete(items.map { it.toData() })
    }
}
