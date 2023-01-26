package br.com.deepbyte.overview.data.model

import com.squareup.moshi.Moshi

data class DiscoverParams(
    val apiId: Long = 0,
    val screenTitle: String = "",
    val mediaId: Long = 0,
    val mediaType: String = ""
) {
    fun toJson(): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<Any>(DiscoverParams::class.java)
        return jsonAdapter.toJson(this)
    }
}
