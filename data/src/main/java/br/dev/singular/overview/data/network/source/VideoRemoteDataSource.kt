package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.VideoDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.response.ListResponse
import javax.inject.Inject

interface IVideoRemoteDataSource {
    suspend fun getVideos(id: Long, type: MediaDataType): DataResult<ListResponse<VideoDataModel>>
}

class VideoRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IVideoRemoteDataSource {

    override suspend fun getVideos(
        id: Long,
        type: MediaDataType
    ): DataResult<ListResponse<VideoDataModel>> {
        val response = api.getVideos(mediaType = type.key, id = id)
        return responseToResult(response)
    }
}
