package br.com.deepbyte.overview.data.repository.media

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.api.response.PagingMediaResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaPagingSource
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

typealias PagingMediaResult = DataResult<PagingMediaResponse<Media>>

class MediaRepository @Inject constructor(
    private val _movieDataSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowDataSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingDataSource: IStreamingRemoteDataSource,
    @IoDispatcher
    private val _dispatcher: CoroutineDispatcher,
    private val _coroutineScope: CoroutineScope
) : IMediaRepository {

    override suspend fun getItem(apiId: Long, mediaType: String) =
        withContext(_dispatcher) {
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

    fun pagingAllBySuffix(watchProviders: List<Long>) =
        makePaging(
            onRequest = { page: Int ->

                val movies = _movieDataSource.getAllBySuffix(page, watchProviders)
                val tvShows = _tvShowDataSource.getAllBySuffix(page, watchProviders)

                val result = movies.plus(tvShows).sortedByDescending { it.voteAverage }
                DataResult.Success(data = PagingMediaResponse(page, result))
            }
        )

    private fun makePaging(
        onRequest: suspend (page: Int) -> PagingMediaResult
    ): Flow<PagingData<Media>> {
        return Pager(PagingConfig(pageSize = NETWORK_PAGE_SIZE)) {
            MediaPagingSource(onRequest)
        }.flow.cachedIn(_coroutineScope)
    }
    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
