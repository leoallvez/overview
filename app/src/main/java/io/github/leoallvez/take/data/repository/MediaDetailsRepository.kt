package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.source.mediaitem.IMediaRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.util.MediaDetailResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MediaDetailsRepository @Inject constructor(
    private val _dataSource: IMediaRemoteDataSource,
    @IoDispatcher private val _ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getMediaDetails(id: Long, type: String) = flow<MediaDetailResult> {
        _dataSource.getMediaDetails(id = id, type = type)
    }.flowOn(_ioDispatcher)

}
