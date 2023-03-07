package br.com.deepbyte.overview.data.api

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.data.api.response.*
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.PersonDetails
import br.com.deepbyte.overview.data.model.provider.Streaming
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
        apiKey: String = BuildConfig.API_KEY,
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
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<ListResponse<TvShow>, ErrorResponse>

    @GET(value = "{url}")
    suspend fun getTvShowItems(
        @Path(value = "url", encoded = true)
        url: String,
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ListResponse<TvShow>, ErrorResponse>

    // Movie
    @GET(value = "movie/{api_id}")
    suspend fun getMovie(
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

    ): NetworkResponse<Movie, ErrorResponse>

    // TODO: refactor - this will be deleted
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
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<ListResponse<Movie>, ErrorResponse>

    // TODO: refactor - this will be deleted
    @GET(value = "{url}")
    suspend fun getMovieItems(
        @Path(value = "url", encoded = true)
        url: String,
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ListResponse<Movie>, ErrorResponse>

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
    ): NetworkResponse<PersonDetails, ErrorResponse>

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

    // Streaming
    @GET(value = "watch/providers/tv")
    suspend fun getStreamingItems(
        @Query(value = "api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ListResponse<Streaming>, ErrorResponse>

    // New requests & labs
    @GET(value = "discover/tv")
    suspend fun getTvShowsBySuffix(
        @Query(value = "with_watch_providers")
        streamingsIds: String = "",
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
    ): NetworkResponse<PagingMediaResponse<TvShow>, ErrorResponse>

    @GET(value = "discover/movie")
    suspend fun getMoviesBySuffix(
        @Query(value = "with_watch_providers")
        streamingsIds: String = "",
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
    ): NetworkResponse<PagingMediaResponse<Movie>, ErrorResponse>
}
