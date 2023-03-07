package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaItemRepository
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaItemRepository @Inject constructor(
    @IoDispatcher
    private val _dispatcher: CoroutineDispatcher,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingSource: IStreamingRemoteDataSource
) : IMediaItemRepository {

    override suspend fun getItem(apiId: Long, type: MediaType) = withContext(_dispatcher) {
        val result = getMedia(apiId, type)
        setStreamings(result)
        flow { emit(result) }
    }

    private suspend fun getMedia(apiId: Long, type: MediaType) = when (type) {
        MediaType.MOVIE -> _movieSource.find(apiId)
        MediaType.TV_SHOW -> _tvShowSource.find(apiId)
        else -> throw IllegalArgumentException("Unsupported media type")
    }

    private suspend fun setStreamings(result: DataResult<out Media>) {
        result.data?.apply {
            streamings = getStreamings(apiId, getType())
        }
    }

    private suspend fun getStreamings(apiId: Long, mediaType: String) =
        _streamingSource.getItems(apiId, mediaType)
}
