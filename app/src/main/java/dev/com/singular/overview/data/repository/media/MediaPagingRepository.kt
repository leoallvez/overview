package dev.com.singular.overview.data.repository.media

import dev.com.singular.overview.data.repository.MediaPagingRepository
import dev.com.singular.overview.data.source.media.remote.IMediaRemoteDataSource
import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.model.media.Movie
import dev.com.singular.overview.data.model.media.TvShow
import dev.com.singular.overview.data.repository.media.interfaces.IMediaPagingRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MediaPagingRepository @Inject constructor(
    coroutineScope: CoroutineScope,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>
) : MediaPagingRepository(coroutineScope), IMediaPagingRepository {

    override fun getMediasPaging(searchFilters: SearchFilters) = filterPaging(searchFilters)

    override suspend fun getMovies(page: Int) = _movieSource.getPaging(page, searchFilters)

    override suspend fun getTVShows(page: Int) = _tvShowSource.getPaging(page, searchFilters)
}
