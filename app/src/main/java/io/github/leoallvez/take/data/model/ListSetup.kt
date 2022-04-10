package io.github.leoallvez.take.data.model

import com.google.gson.annotations.SerializedName

class ListSetup (
    val order: Int,
    @SerializedName("title_resource_id")
    val titleResourceId: String,
    @SerializedName("api_path")
    val apiPath: String,
)
