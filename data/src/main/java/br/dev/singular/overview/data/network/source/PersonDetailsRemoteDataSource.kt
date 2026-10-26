package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.PersonDetailsDataModel
import br.dev.singular.overview.data.network.TmdbApiService
import javax.inject.Inject

interface IPersonDetailsRemoteDataSource {
    suspend fun getById(id: Long): DataResult<PersonDetailsDataModel>
}

class PersonDetailsRemoteDataSource @Inject constructor(
    private val api: TmdbApiService
) : IPersonDetailsRemoteDataSource {

    override suspend fun getById(id: Long): DataResult<PersonDetailsDataModel> {
        val response = api.getPersonDetailsById(id = id)
        return responseToResult(response)
    }
}
