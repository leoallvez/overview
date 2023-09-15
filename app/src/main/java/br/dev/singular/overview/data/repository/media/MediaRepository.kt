package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.media.interfaces.IMediaRepository
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import br.dev.singular.overview.data.source.streaming.IStreamingRemoteDataSource
import br.dev.singular.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    @IoDispatcher
    private val _dispatcher: CoroutineDispatcher,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingSource: IStreamingRemoteDataSource
) : IMediaRepository {

    override suspend fun getItem(apiId: Long, type: MediaTypeEnum) = withContext(_dispatcher) {
        val result = getMedia(apiId, type)
        setStreamings(result)
        flow { emit(result) }
    }

    private suspend fun getMedia(apiId: Long, type: MediaTypeEnum) = when (type) {
        MediaTypeEnum.MOVIE -> _movieSource.find(apiId)
        MediaTypeEnum.TV_SHOW -> _tvShowSource.find(apiId)
        else -> throw IllegalArgumentException("Unsupported media type")
    }

    private suspend fun setStreamings(result: DataResult<out Media>) {
        result.data?.apply {
            streamings = getStreamings(apiId, getType()).sortedBy { it.priority }
        }
    }

    private suspend fun getStreamings(apiId: Long, mediaType: String) =
        _streamingSource.getItems(apiId, mediaType)
}
