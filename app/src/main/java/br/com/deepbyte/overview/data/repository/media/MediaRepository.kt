package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val _movieDataSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowDataSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingDataSource: IStreamingRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IMediaRepository {

    override suspend fun getItem(
        apiId: Long,
        mediaType: String
    ) = withContext(_dispatcher) {
        val result = requestMedia(apiId, mediaType)
        result.data?.streamings = getStreamings(apiId, mediaType)
        flow { emit(result) }
    }

    private suspend fun requestMedia(apiId: Long, type: String) =
        if (type == MediaType.MOVIE.key) {
            _movieDataSource.find(apiId)
        } else {
            _tvShowDataSource.find(apiId)
        }

    private suspend fun getStreamings(apiId: Long, mediaType: String) =
        _streamingDataSource.getItems(apiId, mediaType)
}
