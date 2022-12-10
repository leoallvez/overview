package br.com.deepbyte.overview.data.model

import com.google.gson.Gson

data class DiscoverParams(
    val apiId: Long = 0,
    val screenTitle: String = "",
    val mediaId: Long = 0,
    val mediaType: String = ""
) {
    fun toJson(): String = Gson().toJson(this)
}
