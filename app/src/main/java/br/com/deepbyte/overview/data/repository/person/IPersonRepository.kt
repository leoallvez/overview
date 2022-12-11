package br.com.deepbyte.overview.data.repository.person

import br.com.deepbyte.overview.data.api.response.PersonDetails
import br.com.deepbyte.overview.data.source.DataResult
import kotlinx.coroutines.flow.Flow

interface IPersonRepository {
    suspend fun getItem(apiId: Long): Flow<DataResult<PersonDetails>>
}
