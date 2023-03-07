package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
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

    override suspend fun getPaging(page: Int, streamingsIds: List<Long>): List<TvShow> {
        val ids = streamingsIds.joinToStringWithPipe()
        return when (val response = paging(page, streamingsIds = ids)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun paging(page: Int, streamingsIds: String) = _locale.run {
        _api.getTvShowsPaging(streamingsIds, page, language, region, region)
    }

    override suspend fun search(query: String) = _locale.run {
        val response = _api.searchTvShow(query, language, region, region)
        responseToResult(response)
    }
}
