package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.source.mediaitem.IMediaRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaDetailsRepository @Inject constructor(
    private val _dataSource: IMediaRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher,
) {

    suspend fun getMediaDetailsResult(apiId: Long, mediaType: String) = withContext(_dispatcher) {
        return@withContext flow{
            val providers = _dataSource.getProvidersResult(apiId, mediaType).data?.results
            emit(_dataSource.getMediaDetailsResult(apiId, mediaType))
        }
    }
}
