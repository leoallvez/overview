package dev.com.singular.overview.data.repository.search

import dev.com.singular.overview.data.repository.MediaPagingRepository
import dev.com.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.model.media.Movie
import dev.com.singular.overview.data.model.media.TvShow
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SearchMediaPagingRepository @Inject constructor(
    _coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : MediaPagingRepository(_coroutineScope), ISearchPagingRepository {

    override fun searchPaging(searchFilters: SearchFilters) = filterPaging(searchFilters)

    override suspend fun getMovies(page: Int) = _movieSource.searchPaging(page, searchFilters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.searchPaging(page, searchFilters)
}
