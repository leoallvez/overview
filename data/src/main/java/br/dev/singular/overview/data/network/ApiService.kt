package br.dev.singular.overview.data.network

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.ListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(value = "{path}")
    suspend fun getMediasByPath(
        @Path(value = "path", encoded = true)
        path: String,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<MediaDataPage, ErrorResponse>

    @GET(value = "person/{api_id}")
    suspend fun getPersonById(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = "",
        @Query(value = "append_to_response")
        appendToResponse: String = "tv_credits,movie_credits"
    ): NetworkResponse<PersonDataModel, ErrorResponse>

    @GET(value = "watch/providers/tv")
    suspend fun getStreaming(
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ListResponse<StreamingDataModel>, ErrorResponse>

    private companion object {
        const val API_KEY = BuildConfig.API_KEY
    }
}
