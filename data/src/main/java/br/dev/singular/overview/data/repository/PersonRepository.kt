package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPersonRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.domain.repository.GetById
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val dataSource: IPersonRemoteDataSource
) : GetById<Person> {

    override suspend fun getById(id: Long): Person? {

        return when (val response = dataSource.getById(id)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> null
        }
    }
}
