package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.data.network.ApiService
import javax.inject.Inject

interface IPersonRemoteDataSource {
    suspend fun getById(id: Long): DataResult<PersonDataModel>
}

class PersonRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IPersonRemoteDataSource {

    override suspend fun getById(id: Long): DataResult<PersonDataModel> {
        val response = api.getPersonById(id = id)
        return responseToResult(response)
    }
}
