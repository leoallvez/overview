package io.github.leoallvez.take.data.source

import io.github.leoallvez.take.data.model.AudioVisualItem

sealed class AudioVisualResult {

    class ApiSuccess(
        val content: List<AudioVisualItem>
    ) : AudioVisualResult()

    class ApiError(
        val code: Int,
        val message: String?
    ) : AudioVisualResult()

    object ApiNetworkError : AudioVisualResult()

    object ApiUnknownError : AudioVisualResult()
}