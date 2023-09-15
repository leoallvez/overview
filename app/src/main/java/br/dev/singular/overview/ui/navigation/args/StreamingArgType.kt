package br.dev.singular.overview.ui.navigation.args

import com.squareup.moshi.Moshi
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.util.fromJson

class StreamingArgType : JsonNavType<StreamingEntity>() {
    override fun fromJsonParse(value: String): StreamingEntity {
        return value.fromJson() ?: StreamingEntity()
    }
    override fun StreamingEntity.getJsonParse(): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<Any>(StreamingEntity::class.java)
        return jsonAdapter.toJson(this)
    }
}
