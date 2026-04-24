package br.dev.singular.overview.data.api

import br.dev.singular.overview.data.BuildConfig
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

    @GET(value = "search/tv")
    suspend fun searchTvShow(
        @Query("query")
        query: String,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "page")
        page: Int = 0,
        @Query(value = "api_key")
        apiKey: String = API_KEY
    ): NetworkResponse<ListResponse<TvShow>, ErrorResponse>

    @GET(value = "discover/tv?sort_by=popularity.desc")
    suspend fun discoverOnTvByStreaming(
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "first_air_date.gte")
        dateIni: String = "",
        @Query(value = "first_air_date.lte")
        dateEnd: String = "",
        @Query(value = "with_watch_providers")
        streamingIds: String = ""
    ): NetworkResponse<ListResponse<TvShow>, ErrorResponse>

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

    @GET(value = "search/movie")
    suspend fun searchMovie(
        @Query("query")
        query: String,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "watch_region")
        watchRegion: String = "",
        @Query(value = "page")
        page: Int = 0,
        @Query(value = "api_key")
        apiKey: String = API_KEY
    ): NetworkResponse<ListResponse<Movie>, ErrorResponse>

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

    companion object {
        private const val API_KEY = BuildConfig.API_KEY
    }
}
