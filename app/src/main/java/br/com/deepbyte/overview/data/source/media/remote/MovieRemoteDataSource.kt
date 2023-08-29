package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.model.filters.SearchFilters
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.source.responseToResult
import br.com.deepbyte.overview.util.joinToStringWithPipe
import br.com.deepbyte.overview.util.toFormatted
import br.com.deepbyte.overview.util.toLastWeekFormatted
import com.haroldadmin.cnradapter.NetworkResponse
import java.util.Date
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource<Movie> {

    override suspend fun find(apiId: Long) = _locale.run {
        val response = _api.getMovie(id = apiId, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun getPaging(page: Int, searchFilters: SearchFilters): List<Movie> {
        return when (val response = makePaging(page, searchFilters)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makePaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        val streamingsIds = searchFilters.streamingsIds.joinToStringWithPipe()
        val genresIds = searchFilters.getGenreIdsSeparatedWithComma()
        _api.getMoviesPaging(streamingsIds, genresIds, page, language, region, region)
    }

    override suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<Movie> {
        return when (val response = makeSearchPaging(page, searchFilters)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makeSearchPaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        _api.searchMovie(searchFilters.query, language, region, region, page)
    }

    override suspend fun discoverByStreamings(streamingsIds: List<Long>): List<Movie> {
        return when (val response = makeDiscover(streamingsIds)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makeDiscover(streamingsIds: List<Long>) = _locale.run {
        val today: Date by lazy { Date() }
        _api.discoverOnMovieByStreamings(
            language = language,
            region = region,
            dateIni = today.toLastWeekFormatted(),
            dateEnd = today.toFormatted(),
            streamingsIds = streamingsIds.joinToStringWithPipe()
        )
    }
}
