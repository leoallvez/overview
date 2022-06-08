package io.github.leoallvez.take.data.source

import io.github.leoallvez.take.data.model.MediaItem

sealed class MediaResult {

    class ApiSuccess(val items: List<MediaItem>) : MediaResult()

    class ApiError(val code: Int, val message: String?) : MediaResult()

    object ApiNetworkError : MediaResult()

    object ApiUnknownError : MediaResult()
}