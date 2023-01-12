package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.model.person.PersonDetails
import br.com.deepbyte.overview.data.source.DataResult

interface IPersonRemoteDataSource {
    suspend fun getItem(apiId: Long): DataResult<PersonDetails>
}
