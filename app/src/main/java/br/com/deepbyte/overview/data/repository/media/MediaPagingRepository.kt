package br.com.deepbyte.overview.data.repository.media

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaPagingSource
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaPagingRepository @Inject constructor(
    private val _coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : IMediaPagingRepository {

    override fun getPaging(streamingsIds: List<Long>) = createPaging(
        onRequest = { page: Int ->

            val movies = _movieSource.getPaging(page, streamingsIds)
            val tvShows = _tvShowSource.getPaging(page, streamingsIds)

            val result = movies.plus(tvShows).sortedByDescending { it.voteAverage }
            DataResult.Success(data = PagingResponse(page, result))
        }
    )

    private fun createPaging(
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
