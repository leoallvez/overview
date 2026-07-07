package br.dev.singular.overview.data.network

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MovieDetailsDataModel
import br.dev.singular.overview.data.model.PersonDetailsDataModel
import br.dev.singular.overview.data.model.TvShowDetailsDataModel
import br.dev.singular.overview.data.model.VideoDataModel
import br.dev.singular.overview.data.model.WatchProvidersDataModel
import br.dev.singular.overview.data.network.response.MapResponse
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.GenreListResponse
import br.dev.singular.overview.data.network.response.ListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

private typealias ApiResp <T> = NetworkResponse<T, ErrorResponse>

interface ApiService {

    @GET(value = "{path}")
    suspend fun fetchMediaPage(
        @Path(value = "path", encoded = true)
        path: String,
        @Query(value = "page")
        page: Int?,
        @Query("query")
        query: String?,
        @QueryMap
        options: Map<String, String>
    ): ApiResp<MediaDataPage>

    @GET(value = "tv/{api_id}")
    suspend fun getTvShowDetailsById(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "append_to_response")
        appendToResponse: String = "credits,similar"
    ): ApiResp<TvShowDetailsDataModel>

    @GET(value = "movie/{api_id}")
    suspend fun getMovieDetailsById(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "append_to_response")
        appendToResponse: String = "credits,similar"
    ): ApiResp<MovieDetailsDataModel>

    @GET(value = "{media_type}/{api_id}/videos")
    suspend fun getVideos(
        @Path(value = "media_type", encoded = true)
        mediaType: String,
        @Path(value = "api_id", encoded = true)
        id: Long,
    ): ApiResp<ListResponse<VideoDataModel>>

    @GET(value = "{media_type}/{api_id}/watch/providers")
    suspend fun getWatchProviders(
        @Path(value = "media_type", encoded = true)
        mediaType: String,
        @Path(value = "api_id", encoded = true)
        id: Long,
    ): ApiResp<MapResponse<WatchProvidersDataModel>>

    @GET(value = "person/{api_id}")
    suspend fun getPersonDetailsById(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "append_to_response")
        appendToResponse: String = "tv_credits,movie_credits"
    ): ApiResp<PersonDetailsDataModel>

    @GET(value = "watch/providers/tv")
    suspend fun getCatalog(
        @Query("watch_region")
        region: String
    ): ApiResp<ListResponse<CatalogDataModel>>

    @GET(value = "genre/{media_type}/list")
    suspend fun getGenres(
        @Path(value = "media_type", encoded = true)
        mediaType: String
    ): ApiResp<GenreListResponse>
}
