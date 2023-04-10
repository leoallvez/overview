package br.com.deepbyte.overview.data.repository.media

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.Filters
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaPagingSource
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.*
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import br.com.deepbyte.overview.util.PagingMediaResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaPagingRepository @Inject constructor(
    private val _coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : IMediaPagingRepository {

    override fun getMediasPaging(filters: Filters) =
        createPaging(
            onRequest = { page: Int ->
                if (page > 0) {
                    val result = when (filters.mediaType) {
                        MOVIE -> getMovies(page, filters)
                        TV_SHOW -> getTVShows(page, filters)
                        ALL -> mergeMedias(page, filters)
                    }
                    DataResult.Success(data = PagingResponse(page, result))
                } else {
                    DataResult.UnknownError()
                }
            }
        )

    private suspend fun mergeMedias(page: Int, filters: Filters): List<Media> {
        val movies = getMovies(page, filters)
        val tvShows = getTVShows(page, filters)
        return movies.plus(tvShows).sortedByDescending { it.voteAverage }
    }

    private suspend fun getMovies(page: Int, filters: Filters) =
        _movieSource.getPaging(page, filters.streamingsIds)

    private suspend fun getTVShows(page: Int, filters: Filters) =
        _tvShowSource.getPaging(page, filters.streamingsIds)

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
