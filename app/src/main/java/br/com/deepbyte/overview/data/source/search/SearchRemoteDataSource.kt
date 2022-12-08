package br.com.deepbyte.overview.data.source.search

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.SearchMediaResponse
import br.com.deepbyte.overview.data.api.response.SearchPersonResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

typealias SearchMediasResult = DataResult<SearchMediaResponse>
typealias SearchPersonsResult = DataResult<SearchPersonResponse>

interface ISearchRemoteDataSource {
    suspend fun searchMedia(mediaType: String, query: String): SearchMediasResult
    suspend fun searchPerson(query: String): SearchPersonsResult
}

class SearchRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : ISearchRemoteDataSource {

    override suspend fun searchMedia(mediaType: String, query: String) = _locale.run {
        responseToResult(_api.searchMedia(mediaType, query, language, region, region))
    }

    override suspend fun searchPerson(query: String) = _locale.run {
        responseToResult(_api.searchPerson(query, language, region, region))
    }
}
