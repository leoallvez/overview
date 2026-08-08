package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPersonDetailsRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.PersonDetails
import br.dev.singular.overview.domain.repository.GetById
import javax.inject.Inject

class PersonDetailsRepository @Inject constructor(
    private val dataSource: IPersonDetailsRemoteDataSource
) : GetById<PersonDetails> {

    override suspend fun getById(id: Long): PersonDetails? {

        return when (val response = dataSource.getById(id)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> null
        }
    }
}
