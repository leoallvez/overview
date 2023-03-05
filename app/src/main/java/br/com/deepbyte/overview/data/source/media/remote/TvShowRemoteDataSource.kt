package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.responseToResult
import br.com.deepbyte.overview.util.joinToStringWithPipe
import javax.inject.Inject

class TvShowRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource<TvShow> {

    override suspend fun find(apiId: Long) = _locale.run {
        val response = _api.getTvShow(id = apiId, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun pagingAllBySuffix(
        page: Int,
        urlSuffix: String,
        watchProviders: List<Long>
    ): DataResult<PagingResponse<TvShow>> {
        val providers = watchProviders.joinToStringWithPipe()
        val response = getBySuffix(page, urlSuffix, providers)
        return responseToResult(response)
    }

    private suspend fun getBySuffix(
        page: Int,
        urlSuffix: String,
        watchProviders: String
    ) = _locale.run {
        _api.getTvShowsBySuffix(urlSuffix, watchProviders, page, language, region, region)
    }

    override suspend fun search(query: String) = _locale.run {
        val response = _api.searchTvShow(query, language, region, region)
        responseToResult(response)
    }
}
