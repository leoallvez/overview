package br.dev.singular.overview.data.repository.media.remote

import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.MediaPagingRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaSearchPagingRepository
import br.dev.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import javax.inject.Inject

class MediaSearchPagingRepository @Inject constructor(
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : MediaPagingRepository(), IMediaSearchPagingRepository {

    override fun searchPaging(filters: SearchFilters) = filterPaging(filters)

    override suspend fun getMovies(page: Int) = _movieSource.searchPaging(page, searchFilters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.searchPaging(page, searchFilters)
}
