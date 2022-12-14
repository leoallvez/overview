package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.model.provider.ProviderPlace

interface IProviderRemoteDataSource {
    suspend fun getItems(apiId: Long, type: String): List<ProviderPlace>
}
