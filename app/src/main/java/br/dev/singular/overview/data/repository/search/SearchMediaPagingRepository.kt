package br.dev.singular.overview.data.repository.search

import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.MediaPagingRepository
import br.dev.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SearchMediaPagingRepository @Inject constructor(
    coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : MediaPagingRepository(coroutineScope), ISearchPagingRepository {

    override fun searchPaging(searchFilters: SearchFilters) = filterPaging(searchFilters)

    override suspend fun getMovies(page: Int) = _movieSource.searchPaging(page, searchFilters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.searchPaging(page, searchFilters)
}
