package io.github.leoallvez.take.data.model

import io.github.leoallvez.take.BuildConfig

interface AudioVisual {

    val apiId: Long
    val posterPath: String?
    val voteAverage: Double
    var suggestionId: Long

    fun getContentTitle(): String

    fun getImageUrl() = "${BuildConfig.IMG_URL}$posterPath"

    companion object {
        const val EMPTY_TITLE = ""
    }
}
