package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.media.interfaces.IMediaRepository
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.data.source.media.local.MediaLocalDataSource
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
    private val _mediaLocalDataSource: MediaLocalDataSource,
    private val _movieRemoteSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowRemoteSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingSource: IStreamingRemoteDataSource
) : IMediaRepository {

    override suspend fun getItem(apiId: Long, type: MediaTypeEnum) = withContext(_dispatcher) {
        val result = getMedia(apiId, type)
        setStreaming(result)
        flow { emit(result) }
    }

    override suspend fun update(media: MediaEntity) = withContext(_dispatcher) {
        _mediaLocalDataSource.update(media)
    }

    private suspend fun getMedia(apiId: Long, type: MediaTypeEnum) = when (type) {
        MediaTypeEnum.MOVIE -> _movieRemoteSource.find(apiId)
        MediaTypeEnum.TV_SHOW -> _tvShowRemoteSource.find(apiId)
        else -> throw IllegalArgumentException("Unsupported media type")
    }

    private suspend fun setStreaming(result: DataResult<out Media>) {
        result.data?.apply {
            streamings = getStreaming(apiId, getType()).sortedBy { it.priority }
        }
    }

    private suspend fun getStreaming(apiId: Long, mediaType: String) =
        _streamingSource.getItems(apiId, mediaType)
}
