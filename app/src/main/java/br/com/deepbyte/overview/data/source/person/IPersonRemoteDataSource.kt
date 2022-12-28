package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.model.person.PersonDetails
import br.com.deepbyte.overview.data.source.DataResult

interface IPersonRemoteDataSource {
    suspend fun getItem(apiId: Long): DataResult<PersonDetails>
    suspend fun search(query: String): DataResult<ListResponse<Person>>
}