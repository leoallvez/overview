package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.ApiResult
import io.github.leoallvez.take.data.source.parserResponseToResult
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getMediaItems(url: String): ApiResult<ListContentResponse<MediaItem>>
    suspend fun getMediaDetailsResult(id: Long, type: String): ApiResult<MediaDetailResponse>
}

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService
) : IMediaRemoteDataSource {

    override suspend fun getMediaItems(url: String) =
        parserResponseToResult(response = _api.getMediaItems(url = url))

    override suspend fun getMediaDetailsResult(id: Long, type: String) =
        parserResponseToResult(response = _api.requestMediaDetail(id = id, type = type))

}

