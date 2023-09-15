package dev.com.singular.overview.data.repository.person

import dev.com.singular.overview.data.model.person.Person
import dev.com.singular.overview.data.source.DataResult
import kotlinx.coroutines.flow.Flow

interface IPersonRepository {
    suspend fun getItem(apiId: Long): Flow<DataResult<Person>>
}
