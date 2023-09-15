package dev.com.singular.overview.data.source.person

import dev.com.singular.overview.data.model.person.Person
import dev.com.singular.overview.data.source.DataResult

interface IPersonRemoteDataSource {
    suspend fun getItem(apiId: Long): DataResult<Person>
}
