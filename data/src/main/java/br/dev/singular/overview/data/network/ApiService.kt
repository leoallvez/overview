package br.dev.singular.overview.data.network

import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.ListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private typealias ApiResp <T> = NetworkResponse<T, ErrorResponse>

interface ApiService {

    @GET(value = "{path}")
    suspend fun getMediasByPath(
        @Path(value = "path", encoded = true)
        path: String,
    ): ApiResp<MediaDataPage>

    @GET(value = "person/{api_id}")
    suspend fun getPersonById(
        @Path(value = "api_id", encoded = true)
        id: Long,
        @Query(value = "append_to_response")
        appendToResponse: String = "tv_credits,movie_credits"
    ): ApiResp<PersonDataModel>

    @GET(value = "watch/providers/tv")
    suspend fun getStreaming(): ApiResp<ListResponse<StreamingDataModel>>

}
