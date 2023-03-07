package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.source.responseToResult
import br.com.deepbyte.overview.util.joinToStringWithPipe
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

    override suspend fun getAllBySuffix(
        page: Int,
        watchProviders: List<Long>
    ): List<Movie> {
        val providers = watchProviders.joinToStringWithPipe()
        return when (val response = pagingAllBySuffix(page, providers)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun pagingAllBySuffix(page: Int, watchProviders: String) = _locale.run {
        _api.getMoviesBySuffix(watchProviders, page, language, region, region)
    }

    override suspend fun search(query: String) = _locale.run {
        val response = _api.searchMovie(query, language, region, region)
        responseToResult(response)
    }
}
