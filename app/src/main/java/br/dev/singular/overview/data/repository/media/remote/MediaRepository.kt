package br.dev.singular.overview.data.repository.media.remote

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaRepository
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaType
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
    private val _mediaLocalSource: MediaLocalDataSource,
    private val _movieRemoteSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowRemoteSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingSource: IStreamingRemoteDataSource
) : IMediaRepository {

    override suspend fun getItem(apiId: Long, type: MediaType) = withContext(_dispatcher) {
        val result = getMedia(apiId, type)
        setMediaData(result)
        flow { emit(result) }
    }

    override suspend fun update(media: Media) = withContext(_dispatcher) {
        _mediaLocalSource.update(media.toMediaEntity())
    }

    private suspend fun getMedia(apiId: Long, type: MediaType) = when (type) {
        MediaType.MOVIE -> _movieRemoteSource.find(apiId)
        MediaType.TV_SHOW -> _tvShowRemoteSource.find(apiId)
        else -> throw IllegalArgumentException("Unsupported media type")
    }

    private suspend fun setMediaData(result: DataResult<out Media>) {
        result.data?.apply {
            isLiked = _mediaLocalSource.isLiked(apiId)
            streamings = getStreaming(apiId, getType())
        }
    }

    private suspend fun getStreaming(apiId: Long, mediaType: String) =
        _streamingSource.getItems(apiId, mediaType).sortedBy { it.priority }
}
