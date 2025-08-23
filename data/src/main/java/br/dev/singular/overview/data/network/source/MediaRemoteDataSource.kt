package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.LocaleProvider
import br.dev.singular.overview.data.network.response.ListResponse
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getByPath(path: String): DataResult<ListResponse<MediaDataModel>>
}

class MediaRemoteDataSource @Inject constructor(
    private val api: ApiService,
    private val locale: LocaleProvider
) : IMediaRemoteDataSource {

    override suspend fun getByPath(path: String) = locale.run {
        val response = api.getMediasByPath(path = path, language = language, region = region)
        responseToResult(response)
    }
}
