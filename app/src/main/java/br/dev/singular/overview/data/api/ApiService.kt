package br.dev.singular.overview.data.api

import br.dev.singular.overview.data.BuildConfig.API_KEY
import br.dev.singular.overview.data.api.response.ErrorResponse
import br.dev.singular.overview.data.api.response.ListResponse
import br.dev.singular.overview.data.api.response.ProviderResponse
import br.dev.singular.overview.data.model.media.Movie
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.model.media.Video
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // TV
    @GET(value = "tv/{api_id}")
    suspend fun getTvShow(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "append_to_response")
        appendToResponse: String = "credits,similar"

    ): NetworkResponse<TvShow, ErrorResponse>

    // Movie
    @GET(value = "movie/{api_id}")
    suspend fun getMovie(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "append_to_response")
        appendToResponse: String = "credits,similar"

    ): NetworkResponse<Movie, ErrorResponse>

    // Providers
    @GET(value = "{media_type}/{api_id}/watch/providers")
    suspend fun getProviders(
        @Path(value = "media_type", encoded = true)
        mediaType: String,
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ProviderResponse, ErrorResponse>

    // Trailers
    @GET(value = "{media_type}/{api_id}/videos")
    suspend fun getVideos(
        @Path(value = "media_type", encoded = true)
        mediaType: String,
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = ""
    ): NetworkResponse<ListResponse<Video>, ErrorResponse>
}
