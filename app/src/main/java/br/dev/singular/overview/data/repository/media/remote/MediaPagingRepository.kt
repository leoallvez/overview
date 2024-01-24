package br.dev.singular.overview.data.repository.media.remote

import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.MediaPagingRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaPagingRepository
import br.dev.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import javax.inject.Inject

class MediaPagingRepository @Inject constructor(
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : MediaPagingRepository(), IMediaPagingRepository {

    override fun getPaging(filters: SearchFilters) = filterPaging(filters)

    override suspend fun getMovies(page: Int) = _movieSource.getPaging(page, searchFilters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.getPaging(page, searchFilters)
}
