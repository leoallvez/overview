package br.dev.singular.overview.data.repository.person

import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.data.source.DataResult
import kotlinx.coroutines.flow.Flow

interface IPersonRepository {
    suspend fun getItem(apiId: Long): Flow<DataResult<Person>>
}
