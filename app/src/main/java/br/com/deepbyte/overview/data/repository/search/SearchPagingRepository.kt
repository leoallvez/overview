package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.model.filters.Filters
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.PagingRepository
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SearchPagingRepository @Inject constructor(
    _coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : PagingRepository(_coroutineScope), ISearchPagingRepository {

    override fun searchPaging(filters: Filters) = filterPaging(filters)

    override suspend fun getMovies(page: Int) = _movieSource.searchPaging(page, filters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.searchPaging(page, filters)
}
