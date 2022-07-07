package io.github.leoallvez.take.data.source.mediaitem

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.parserResponseToResult
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getMediaItems(url: String): DataResult<ListContentResponse<MediaItem>>
    suspend fun getMediaDetailsResult(apiId: Long, mediaType: String): DataResult<MediaDetailResponse>
}

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService
) : IMediaRemoteDataSource {

    override suspend fun getMediaItems(url: String) =
        parserResponseToResult(response = _api.getMediaItems(url = url))

    override suspend fun getMediaDetailsResult(apiId: Long, mediaType: String) =
        parserResponseToResult(response = _api.requestMediaDetail(apiId = apiId, mediaType = mediaType))

}

