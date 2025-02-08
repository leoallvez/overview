package br.dev.singular.overview.data.network

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.ListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(value = "{url}")
    suspend fun getMediasByPath(
        @Path(value = "url", encoded = true)
        url: String,
        @Query(value = "api_key")
        apiKey: String = API_KEY,
        @Query(value = "language")
        language: String = "",
        @Query(value = "region")
        region: String = ""
    ): NetworkResponse<ListResponse<MediaDataModel>, ErrorResponse>

    private companion object {
        const val API_KEY = BuildConfig.API_KEY
    }
}
