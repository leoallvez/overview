package br.com.deepbyte.overview.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.filters.Filters
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaPagingSource
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.util.PagingMediaResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class PagingRepository(
    private val _coroutineScope: CoroutineScope
) {

    protected lateinit var filters: Filters

    protected fun filterPaging(filters: Filters) =
        createPaging(
            onRequest = { page: Int ->
                if (page > 0) {
                    this.filters = filters
                    val result = when (filters.mediaType) {
                        MediaTypeEnum.MOVIE -> getMovies(page)
                        MediaTypeEnum.TV_SHOW -> getTVShows(page)
                        MediaTypeEnum.ALL -> getMergedMedias(page)
                    }
                    DataResult.Success(data = PagingResponse(page, result))
                } else {
                    DataResult.UnknownError()
                }
            }
        )

    protected abstract suspend fun getMovies(page: Int): List<Movie>

    protected abstract suspend fun getTVShows(page: Int): List<TvShow>

    private fun createPaging(
        onRequest: suspend (page: Int) -> PagingMediaResult
    ): Flow<PagingData<Media>> {
        return Pager(PagingConfig(pageSize = NETWORK_PAGE_SIZE)) {
            MediaPagingSource(onRequest)
        }.flow.cachedIn(_coroutineScope)
    }

    private suspend fun getMergedMedias(page: Int) =
        getMovies(page).plus(getTVShows(page)).sortedByDescending { it.voteAverage }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
