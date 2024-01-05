package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.source.responseToResult
import com.haroldadmin.cnradapter.NetworkResponse
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
            is NetworkResponse.Success -> {
                response.body.results
            } else -> listOf()
        }
    }

    private suspend fun makePaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        val streamingId = searchFilters.streamingId.toString()
        val genresIds = searchFilters.getGenreIdsSeparatedWithComma()
        _api.getMoviesPaging(streamingId, genresIds, page, language, region, region)
    }

    override suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<Movie> {
        return when (val response = makeSearchPaging(page, searchFilters)) {
            is NetworkResponse.Success -> {
                response.body.results
            } else -> listOf()
        }
    }

    private suspend fun makeSearchPaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        _api.searchMovie(searchFilters.query, language, region, region, page)
    }
}
