package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor (
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource <Movie> {

    override suspend fun find(apiId: Long) = _locale.run {
        val response = _api.getMovie(id = apiId, language = language, region = region)
        responseToResult(response)
    }

    // to use in future
    override suspend fun getItemsByUrl(url: String) = _locale.run {
        val response = _api.getMovieItems(url = url, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun search(query: String) = _locale.run {
        val response = _api.searchMovie(query, language, region, region)
        responseToResult(response)
    }
}
