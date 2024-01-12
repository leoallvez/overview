package br.dev.singular.overview.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.data.api.response.PagingResponse
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaPagingSource
import br.dev.singular.overview.data.source.media.MediaType.ALL
import br.dev.singular.overview.data.source.media.MediaType.MOVIE
import br.dev.singular.overview.data.source.media.MediaType.TV_SHOW
import br.dev.singular.overview.util.PagingMediaResult
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
