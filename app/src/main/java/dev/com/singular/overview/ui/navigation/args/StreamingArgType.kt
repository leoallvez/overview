package dev.com.singular.overview.ui.navigation.args

import dev.com.singular.overview.data.model.provider.StreamingEntity
import dev.com.singular.overview.util.fromJson
import com.squareup.moshi.Moshi

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
