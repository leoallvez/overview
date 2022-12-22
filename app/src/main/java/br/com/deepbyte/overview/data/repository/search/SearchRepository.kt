package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.MediaType.MOVIE
import br.com.deepbyte.overview.data.MediaType.TV_SHOW
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import br.com.deepbyte.overview.util.toList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : ISearchRepository {

    override suspend fun search(query: String) = withContext(_dispatcher) {
        val results = createResults(query)
        flow { emit(results) }
    }

    private suspend fun createResults(query: String): Map<String, List<Media>> {
        val map = mutableMapOf<String, List<Media>>()
        map[MOVIE.key] = _movieSource.search(query).toList()
        map[TV_SHOW.key] = _tvShowSource.search(query).toList()
        return map
    }
}
