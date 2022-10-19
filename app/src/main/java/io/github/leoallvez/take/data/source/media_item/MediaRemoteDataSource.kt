package io.github.leoallvez.take.data.source.media_item

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.api.response.ProviderResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.parserResponseToResult
import javax.inject.Inject

interface IMediaRemoteDataSource {

    suspend fun getMediaItems(url: String): DataResult<ListContentResponse<MediaItem>>

    suspend fun getMediaDetailsResult(
        apiId: Long,
        mediaType: String
    ): DataResult<MediaDetailResponse>

    suspend fun getProvidersResult(
        apiId: Long,
        mediaType: String
    ): DataResult<ProviderResponse>
}

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    _locale: IApiLocale
) : IMediaRemoteDataSource {

    private val language = _locale.language()
    private val region = _locale.region

    override suspend fun getMediaItems(url: String): DataResult<ListContentResponse<MediaItem>> {
        val response = _api.getMediaItems(url = url, language = language, region = region)
        return parserResponseToResult(response)
    }

    override suspend fun getMediaDetailsResult(
        apiId: Long,
        mediaType: String
    ): DataResult<MediaDetailResponse> {
        val response = _api.getMediaDetail(
            apiId = apiId, mediaType = mediaType, language = language, region = region
        )
        return parserResponseToResult(response)
    }

    override suspend fun getProvidersResult(
        apiId: Long,
        mediaType: String
    ): DataResult<ProviderResponse> {
        val response = _api.getProviders(
            apiId = apiId, mediaType = mediaType, language = language, region = region
        )
        return parserResponseToResult(response)
    }

}

