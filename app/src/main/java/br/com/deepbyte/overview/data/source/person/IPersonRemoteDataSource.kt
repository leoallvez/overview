package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.api.response.PersonResponse
import br.com.deepbyte.overview.data.api.response.SearchPersonResponse
import br.com.deepbyte.overview.data.source.DataResult

interface IPersonRemoteDataSource {
    suspend fun getItem(apiId: Long): DataResult<PersonResponse>
    suspend fun search(query: String): DataResult<SearchPersonResponse>
}
