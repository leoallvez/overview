package io.github.leoallvez.take.data.model

import com.google.gson.annotations.SerializedName

data class Suggestions (
    val order: Int,
    @SerializedName(value = "api_path")
    val apiPath: String,
    @SerializedName(value = "title_resource_id")
    val titleResourceId: String,
)
