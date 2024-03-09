package br.dev.singular.overview.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import br.dev.singular.overview.BuildConfig
import br.dev.singular.overview.data.api.response.PagingResponse
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaPagingSource
import br.dev.singular.overview.data.source.media.MediaType.ALL
import br.dev.singular.overview.data.source.media.MediaType.MOVIE
import br.dev.singular.overview.data.source.media.MediaType.TV_SHOW
import br.dev.singular.overview.util.PagingMediaResult

abstract class MediaPagingRepository {

    protected lateinit var searchFilters: SearchFilters

    protected fun filterPaging(filters: SearchFilters) =
        createPaging(
            onRequest = { page: Int ->
                if (page > 0) {
                    searchFilters = filters
                    val result = when (searchFilters.mediaType) {
                        MOVIE -> getMovies(page)
                        TV_SHOW -> getTVShows(page)
                        ALL -> getMergedMedias(page)
                    }
                    DataResult.Success(
                        data = PagingResponse(page, result.map { it.toMediaEntity() })
                    )
                } else {
                    DataResult.UnknownError()
                }
            }
        )

    protected abstract suspend fun getMovies(page: Int): List<Movie>

    protected abstract suspend fun getTVShows(page: Int): List<TvShow>

    private fun createPaging(
        onRequest: suspend (page: Int) -> PagingMediaResult
    ): Pager<Int, MediaEntity> {
        return Pager(PagingConfig(pageSize = BuildConfig.PAGE_SIZE)) {
            MediaPagingSource(onRequest)
        }
    }

    private suspend fun getMergedMedias(page: Int) =
        getMovies(page).plus(getTVShows(page)).sortedByDescending { it.voteAverage }
}
