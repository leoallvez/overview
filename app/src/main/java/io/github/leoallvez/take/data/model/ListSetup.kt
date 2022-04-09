package io.github.leoallvez.take.data.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ListSetup (
    @SerializedName("list_name")
    val listName: String
)