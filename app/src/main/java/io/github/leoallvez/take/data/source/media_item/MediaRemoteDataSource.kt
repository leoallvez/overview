package io.github.leoallvez.take.data.source.media_item

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.responseToResult
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getMediaItemsResult(url: String): DataResult<ListContentResponse<MediaItem>>
    suspend fun getMediaDetailsResult(id: Long, type: String): DataResult<MediaDetailResponse>
}

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource {

    override suspend fun getMediaItemsResult(url: String) = responseToResult(
        _api.getMediaItems(url = url, language = _locale.language, region = _locale.region)
    )

    override suspend fun getMediaDetailsResult(id: Long, type: String) = responseToResult(
        _api.getMediaDetail(
            id = id, type = type, language = _locale.language, region = _locale.region
        )
    )
}
