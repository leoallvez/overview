package io.github.leoallvez.take.data.source

import io.github.leoallvez.take.data.model.AudioVisualItem

sealed class AudiovisualResult {

    class ApiSuccess(
        val content: List<AudioVisualItem>
    ) : AudiovisualResult()

    class ApiError(
        val code: Int,
        val message: String?
    ) : AudiovisualResult()

    object ApiNetworkError : AudiovisualResult()

    object ApiUnknownError : AudiovisualResult()
}