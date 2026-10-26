package br.dev.singular.overview.data.network

import br.dev.singular.overview.data.network.request.JevRequest
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.JevResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface JevApiService {

    @POST(value = "v1/systemone")
    suspend fun decide(
        @Body
        request: JevRequest
    ): NetworkResponse<JevResponse, ErrorResponse>
}
