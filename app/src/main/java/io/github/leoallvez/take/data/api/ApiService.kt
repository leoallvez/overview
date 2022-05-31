package io.github.leoallvez.take.data.api

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.BuildConfig
import io.github.leoallvez.take.data.api.response.ContentResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.model.MediaItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(value = "{url}")
    suspend fun getAudioVisualItems(
        @Path(value = "url", encoded = true)
        url: String,
        @Query(value ="api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "pt-BR",
        @Query(value = "region")
        region: String = "BR"
    ): NetworkResponse<ContentResponse<MediaItem>, ErrorResponse>

}
