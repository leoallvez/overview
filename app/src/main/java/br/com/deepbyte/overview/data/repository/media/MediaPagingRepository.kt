package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.model.filters.Filters
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.PagingRepository
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MediaPagingRepository @Inject constructor(
    _coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : PagingRepository(_coroutineScope), IMediaPagingRepository {

    override fun getMediasPaging(filters: Filters) = filterPaging(filters)

    override suspend fun getMovies(page: Int) = _movieSource.getPaging(page, filters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.getPaging(page, filters)
}
