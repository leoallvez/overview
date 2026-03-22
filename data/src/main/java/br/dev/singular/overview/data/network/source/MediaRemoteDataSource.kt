package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.ILocaleProvider
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getByPath(path: String): DataResult<MediaDataPage>
}

class MediaRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IMediaRemoteDataSource {

    override suspend fun getByPath(path: String): DataResult<MediaDataPage> {
        val response = api.getMediasByPath(path = path)
        return responseToResult(response)
    }
}
