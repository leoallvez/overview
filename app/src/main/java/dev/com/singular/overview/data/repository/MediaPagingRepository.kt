package dev.com.singular.overview.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.com.singular.overview.data.source.DataResult
import dev.com.singular.overview.data.source.media.MediaPagingSource
import dev.com.singular.overview.data.source.media.MediaTypeEnum.ALL
import dev.com.singular.overview.data.source.media.MediaTypeEnum.MOVIE
import dev.com.singular.overview.data.source.media.MediaTypeEnum.TV_SHOW
import dev.com.singular.overview.util.PagingMediaResult
import dev.com.singular.overview.data.api.response.PagingResponse
import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.model.media.Media
import dev.com.singular.overview.data.model.media.Movie
import dev.com.singular.overview.data.model.media.TvShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class MediaPagingRepository(
    private val _coroutineScope: CoroutineScope
) {

    protected lateinit var searchFilters: SearchFilters

    protected fun filterPaging(newSearchFilters: SearchFilters) =
        createPaging(
            onRequest = { page: Int ->
                if (page > 0) {
                    searchFilters = newSearchFilters
                    val result = when (searchFilters.mediaType) {
                        MOVIE -> getMovies(page)
                        TV_SHOW -> getTVShows(page)
                        ALL -> getMergedMedias(page)
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
