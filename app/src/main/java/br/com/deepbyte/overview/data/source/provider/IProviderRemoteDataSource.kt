package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.source.DataResult

interface IProviderRemoteDataSource {
    suspend fun getItems(id: Long, type: String): DataResult<ProviderResponse>
}
