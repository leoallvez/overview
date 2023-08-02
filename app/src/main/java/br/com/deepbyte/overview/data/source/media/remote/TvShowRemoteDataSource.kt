package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.model.filters.SearchFilters
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.responseToResult
import br.com.deepbyte.overview.util.joinToStringWithPipe
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class TvShowRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource<TvShow> {

    override suspend fun find(apiId: Long) = _locale.run {
        val response = _api.getTvShow(id = apiId, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun getPaging(page: Int, searchFilters: SearchFilters): List<TvShow> {
        return when (val response = makePaging(page, searchFilters)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makePaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        val streamingsIds = searchFilters.streamingsIds.joinToStringWithPipe()
        val genresIds = searchFilters.getGenreIdsSeparatedWithComma()
        _api.getTvShowsPaging(streamingsIds, genresIds, page, language, region, region)
    }

    override suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<TvShow> {
        return when (val response = makeSearchPaging(page, searchFilters)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makeSearchPaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        _api.searchTvShow(searchFilters.query, language, region, region, page)
    }
}
