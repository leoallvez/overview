package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.LocaleProvider
import javax.inject.Inject

interface IPersonRemoteDataSource {
    suspend fun getById(id: Long): DataResult<PersonDataModel>
}

class PersonRemoteDataSource @Inject constructor(
    private val api: ApiService,
    private val locale: LocaleProvider
) : IPersonRemoteDataSource {

    override suspend fun getById(id: Long) = locale.run {
        val response = api.getPersonById(id = id, language = language, region = region)
        responseToResult(response)
    }
}
