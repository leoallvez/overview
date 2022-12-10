package br.com.deepbyte.overview.data.api

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.data.api.response.SearchPersonResponse
import br.com.deepbyte.overview.data.api.response.SearchMediaResponse
import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.api.response.ErrorResponse
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.api.response.PersonResponse
import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.MediaItem
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Media
    @GET(value = "{media_type}/{api_id}")
    suspend fun getMediaItem(
        @Path(value = "media_type", encoded = true)
        type: String,
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "append_to_response")
        appendToResponse: String = "credits,similar"

    ): NetworkResponse<MediaDetailResponse, ErrorResponse>

    @GET(value = "{url}")
    suspend fun getMediaItems(
        @Path(value = "url", encoded = true)
        url: String,
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ListResponse<MediaItem>, ErrorResponse>

    @GET(value = "search/{media_type}")
    suspend fun searchMedia(
        @Path(value = "media_type", encoded = true)
        type: String,
        @Query("query")
        query: String,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<SearchMediaResponse, ErrorResponse>

    // Providers
    @GET(value = "{media_type}/{api_id}/watch/providers")
    suspend fun getProviders(
        @Path(value = "media_type", encoded = true)
        type: String,
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ProviderResponse, ErrorResponse>

    // Person
    @GET(value = "person/{api_id}")
    suspend fun getPersonItem(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "append_to_response")
        appendToResponse: String = "tv_credits,movie_credits"
    ): NetworkResponse<PersonResponse, ErrorResponse>

    @GET(value = "search/person")
    suspend fun searchPerson(
        @Query("query")
        query: String,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<SearchPersonResponse, ErrorResponse>

    // Discover
    @GET(value = "tv/popular")
    suspend fun discoverByProvider(
        @Query(value = "with_watch_providers")
        providerId: Long,
        @Query(value = "page")
        page: Int = 0,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<PagingResponse<MediaItem>, ErrorResponse>

    @GET(value = "discover/{media_type}")
    suspend fun discoverByGenre(
        @Path(value = "media_type", encoded = true)
        type: String,
        @Query(value = "with_genres")
        genreId: Long,
        @Query(value = "page")
        page: Int = 0,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<PagingResponse<MediaItem>, ErrorResponse>
}
