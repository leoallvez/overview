package io.github.leoallvez.take.data.source

import io.github.leoallvez.take.data.model.AudioVisual

sealed class AudiovisualResult {

    class ApiSuccess(
        val content: List<AudioVisual>
    ) : AudiovisualResult()

    class ApiError(
        val code: Int,
        val message: String?
    ) : AudiovisualResult()

    object ApiNetworkError : AudiovisualResult()

    object ApiUnknownError : AudiovisualResult()
}