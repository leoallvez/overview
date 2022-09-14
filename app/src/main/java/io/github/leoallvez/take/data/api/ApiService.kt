package io.github.leoallvez.take.data.api

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.model.MediaItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(value = "{url}")
    suspend fun getMediaItems(
        @Path(value = "url", encoded = true)
        url: String,
        @Query(value ="api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "pt-BR",
        @Query(value = "region")
        region: String = "BR"
    ): NetworkResponse<ListContentResponse<MediaItem>, ErrorResponse>

    @GET(value = "{media_type}/{api_id}")
    suspend fun requestMediaDetail(
        @Path(value = "media_type", encoded = true)
        mediaType: String,
        @Path(value = "api_id", encoded = true)
        apiId: Long,
        @Query(value ="api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "pt-BR",
        @Query(value = "region")
        region: String = "BR",
        @Query(value = "append_to_response")
        appendToResponse: String = "credits,similar"

    ): NetworkResponse<MediaDetailResponse, ErrorResponse>

}
