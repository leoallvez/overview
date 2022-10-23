package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json

open class DataResponse(
    @field:Json(name = "id")
    val apiId: Long = 0
)
