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

    suspend fun getMediaDetailsResult(id: Long, type: String) = withContext(_dispatcher) {
        return@withContext flow{
            emit(_dataSource.getMediaDetailsResult(id = id, type = type))
        }
    }
}
