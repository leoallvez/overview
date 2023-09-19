package br.dev.singular.overview.data.source.person

import br.dev.singular.overview.data.model.person.Person
import br.dev.singular.overview.data.source.DataResult

interface IPersonRemoteDataSource {
    suspend fun getItem(apiId: Long): DataResult<Person>
}
