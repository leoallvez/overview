package io.github.leoallvez.take.data.source.mock

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.response.ErrorResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse

internal typealias Response = NetworkResponse<MediaDetailResponse, ErrorResponse>
internal typealias MediaDetailsSuccessResponse = NetworkResponse.Success<MediaDetailResponse>

internal typealias ServerErrorResponse = NetworkResponse.ServerError<ErrorResponse>
internal typealias NetworkErrorResponse = NetworkResponse.NetworkError
internal typealias UnknownErrorResponse = NetworkResponse.UnknownError
