package br.com.deepbyte.overview.ui.navigation.args

import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.util.fromJson
import com.squareup.moshi.Moshi

class StreamingArgType : JsonNavType<Streaming>() {
    override fun fromJsonParse(value: String): Streaming {
        return value.fromJson() ?: Streaming()
    }
    override fun Streaming.getJsonParse(): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<Any>(Streaming::class.java)
        return jsonAdapter.toJson(this)
    }
}