package br.com.deepbyte.overview.data.source.search

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.SearchResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

typealias ApiSearchResponse = DataResult<SearchResponse>

interface ISearchRemoteDataSource {
    suspend fun searchMedia(mediaType: String, query: String, page: Int): ApiSearchResponse
}

class SearchRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : ISearchRemoteDataSource {

    override suspend fun searchMedia(mediaType: String, query: String, page: Int) =
        responseToResult(search(mediaType, query, page))

    private suspend fun search(mediaType: String, query: String, page: Int) = _locale.run {
        _api.searchMedia(mediaType, query, page, language, region, region)
    }
}
