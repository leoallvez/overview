package io.github.leoallvez.take.data.source

import io.github.leoallvez.take.data.model.Audiovisual

sealed class AudiovisualResult {

    class ApiSuccess(
        result: List<Audiovisual>
    ) : AudiovisualResult()

    class ApiError(
        val code: Int,
        val message: String?
    ) : AudiovisualResult()

    object ApiNetworkError : AudiovisualResult()

    object ApiUnknownError : AudiovisualResult()
}